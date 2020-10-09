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
    try {
    let query = `
    SELECT 
    infos.id, infos.shopname, infos.address, infos.grade_avg, 
    main_photo, photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8, photo9, photo10,
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

        let make_json = function (info) {
            return new Promise ( resolve => {
                let json = {};
                json.id = info.id;
                json.shopname = info.shopname;
                json.address = info.address;
                json.grade_avg = info.grade_avg;
                json.main_photo = 'null';
                if(info.main_photo)
                {
                    let key = 'photo' + info.main_photo;
                    json.main_photo = info[key];
		    resolve(json);
                }
                else 
                {
                    resolve(json);
                }
            });
        }
        
        return new Promise( resolve => {
            let new_infos = [];
            let info_array = infos;
            for(let i = 0; i <= info_array.length; i++)
            {
                if(i == info_array.length)
                    resolve(new_infos);
		else {
                    make_json(info_array[i])
                    .then(json => {
                        new_infos.push(json);
                    });
		}
            }
        })
    })

    .then( infos => {

        let json = {'hot': JSON.stringify(infos)};
        client.hmset('recommend',  json);
	
	let query = `
    SELECT 
        infos.id, infos.shopname, infos.address, infos.grade_avg,
        main_photo, photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8, photo9, photo10
    FROM infos
    GROUP BY infos.id
    ORDER BY grade_avg DESC
    LIMIT 0, 9;
    `

    model.sequelize.query(
        query,
        {                                                                                                    replacements: {                                                                                      'yesterday': yesterday,
                'today': today
            },                                                                                               type: QueryTypes.SELECT
        }
    )

    .then( infos =>{
        let make_json = function (info) {
            return new Promise ( resolve => {
                let json = {};
                json.id = info.id;                                                                               json.shopname = info.shopname;
                json.address = info.address;
                json.grade_avg = info.grade_avg;
                json.main_photo = 'null';
                if(info.main_photo)
                {
                    let key = 'photo' + info.main_photo;
                    json.main_photo = info[key];
                    resolve(json);
                }
                else
                {
                    resolve(json);
                }
	    });
        }                                                                                      
        return new Promise( resolve => {                                                                     let new_infos = [];
            let info_array = infos;
            for(let i = 0; i <= info_array.length; i++)
            {
                if(i == info_array.length)
                    resolve(new_infos);
                else { 
                    make_json(info_array[i])
                    .then(json => {
                        new_infos.push(json);
                    });
		}
            }
        })
    })

    .then( infos => {

        let json = {'food': JSON.stringify(infos)};
        client.hmset('recommend',  json);

        query = `
        SELECT 
        courses.id, courses.title, courses.contents, courses.grade_avg, courses.dday, main_photo,
        courses.course_info1 AS shop_id1, courses.shopname1,
        courses.course_info2 AS shop_id2, courses.shopname2, 
        courses.course_info3 AS shop_id3, courses.shopname3, 
        DATE_FORMAT(courses.created_at,'%Y-%m-%d') AS created_at,
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

            return new Promise( resolve => {
                let course_array = courses;
                for(let i = 0; i <= course_array.length; i++)
                {
                    if( i == course_array.length )
                    {
                        resolve(course_array);
                    }
                    course_array[i].shops = [
                        {
                            shop_id: course_array[i].shop_id1,
                            shopname: course_array[i].shopname1
                        },
                        {
                            shop_id: course_array[i].shop_id2,
                            shopname: course_array[i].shopname2
                        },
                        {
                            shop_id: course_array[i].shop_id3,
                            shopname: course_array[i].shopname3
                        }
                    ];
                    delete course_array[i].shop_id1;
                    delete course_array[i].shopname1;
                    delete course_array[i].shop_id2;
                    delete course_array[i].shopname2;
                    delete course_array[i].shop_id3;
                    delete course_array[i].shopname3;
                }
            })
        })
        
        .then(courses => {
            let json = {'courses': JSON.stringify(courses) }
            client.hmset('recommend', json);
        });
    });
    });
    }
    catch (err) {
       console.log(err);
    }
}
