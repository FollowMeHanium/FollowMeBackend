const {Info, InfoLike} = require('./models');


function getRandomArbitrary(min, max) {
    return new Promise( resolve => {
        resolve(Math.floor(Math.random() * (max - min) + min));
    })
}
function insert0 (num) {
    return (num <10) ? ("0"+num) : num;
}

var autoCreate = async function () {
        for(let i=0; i<25; i++)
        {
            let user_id = await getRandomArbitrary(3,5);
            let info_id = await getRandomArbitrary(1,300);
            let month = await getRandomArbitrary(8, 10);
            let date = await getRandomArbitrary(1,30);
            let created_at = "2020-" + insert0(month) + "-" + insert0(date) + " 00:00:00";

            InfoLike.findOne({
                where: {
                    info_id: info_id,
                    user_id: user_id
                }
            })

            .then( info_like => {
                if(!info_like)
                {
                    return InfoLike.create({
                        info_id: info_id,
                        user_id: user_id,
                        created_at: created_at
                    })
                }

            });
        }
    }
    autoCreate();
