const env = process.env;
const { Sequelize, sequelize, Op, QueryTypes} = require('sequelize');
const { User, Course, CourseLike, CourseReview, CourseShare, Info } = require('../models');
const model = require('../models');
const redis = require('redis');
const client = redis.createClient(process.env.REDIS_PORT, process.env.REDIS_HOST);

const insert0 = (data) => {
    return (data < 10) ? ("0"+data ) : data;
}

exports.recommend_update = () => {
    console.log('recommend update ....');
    let date = new Date();
    let yesterday = date.getFullYear() + '-' + insert0(date.getMonth()) + '-' + insert0(date.getDate() - 1);
    let today = date.getFullYear() + '-' + insert0(date.getMonth() + 1) + '-' + insert0(date.getDate());

    let query = `
    SELECT 
	infos.id, infos.shopname, infos.address, infos.grade_avg,
    COUNT(info_likes.id) AS likes
    FROM infos
    JOIN info_likes
    ON ( infos.id = info_likes.info_id )
    WHERE ( DATE(info_likes.created_at) >= :yesterday AND DATE(info_likes.created_at) < :today )
    GROUP BY infos.id
    ORDER BY likes DESC
    LIMIT 0, 9;
    `

    model.sequelize.query(
        query,
        {
            replacements: {
                'yesterday': yesterday,
                'today': today
            },
            type: QueryTypes.SELECT
        }
    )

    .then( infos =>{
        let json = {'shops': JSON.stringify(infos)};
        client.hmset('recommend',  json);

        query = `
        SELECT 
        courses.id, courses.title, courses.contents, courses.grade_avg, courses.dday, courses.course_info1, courses.shopname1,
        courses.course_info2, courses.shopname2, courses.course_info3, courses.shopname3, DATE_FORMAT(courses.created_at,'%Y-%m-%d') AS created_at,
        COUNT(course_likes.id) AS likes
        FROM courses
        JOIN course_likes
        ON ( courses.id = course_likes.course_id )
        WHERE ( courses.share = 1 AND (DATE(course_likes.created_at) >= :yesterday AND DATE(course_likes.created_at) < :today))
        GROUP BY courses.id
        ORDER BY likes DESC
        LIMIT 0, 9;
        `;

        model.sequelize.query(
            query,
            {
                replacements: {
                    'yesterday': yesterday,
                    'today': today
                },
                type: QueryTypes.SELECT
            }
        )

        .then( courses =>{
            let json = {'courses': JSON.stringify(courses) }
            client.hmset('recommend', json);
        });
    }) 
}