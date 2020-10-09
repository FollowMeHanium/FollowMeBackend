let {Thema, InfoThema} = require('./models');

function getRandomArbitrary(min, max) {
    return new Promise( resolve => {
        resolve(Math.floor(Math.random() * (max - min) + min));
    })
}
function insert0 (num) {
    return (num <10) ? ("0"+num) : num;
}

let makeThema = () => {
    for(let i = 0; i < 10; i++)
    {
        Thema.create({
            thema_name: 'thema' + i
        })
    }
}

let makeInfoThema = async () => {
    for(let i = 0; i < 50; i++)
    {
        let thema_id = await getRandomArbitrary(1,7);
        let info_id = await getRandomArbitrary(1,40);

        let info_thema = await InfoThema.findOne({
		where: {
			thema_id: thema_id,
			info_id: info_id
		}
	})

	.then(one => {
		if(!one)
		{
			InfoThema.create({
		            info_id: info_id,
		            thema_id: thema_id
		        });
		}
	})
    }
}
makeInfoThema();
