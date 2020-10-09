const { sequelize, Op } = require('sequelize');
const { Course, Info } = require('./models');
const { gang, hong, jongro } = require(`./data`);
const CATEGORY = ['한','일', '양','중','카','술'];
function getRandomArbitrary(min, max) {
    return new Promise( resolve => {
        resolve(Math.floor(Math.random() * (max - min) + min));
    })
}

shopDataInput = async (data, locate) => {
    let data_array = data.split('\n');
    for(let i = 0; i < data_array.length/22; i++)
    {
        let cate = data_array[i*22+1];
        let category = CATEGORY.findIndex( element => element == cate);
        let json = {
            category : category,
            shopname : data_array[i*22],
            address : data_array[i*22+2] + locate,
            menu: '{"'+data_array[i*22+7]+'":"'+ data_array[i*22+11] + '","' + data_array[i*22+8]+'":"'+ data_array[i*22+12] + '","' + data_array[i*22+9]+'":"'+ data_array[i*22+13] + '","' + data_array[i*22+10]+'":"'+ data_array[i*22+14]+'"}',
            operating_time: data_array[i*22+6],
            phone : data_array[i*22+3],
            latitude : data_array[i*22+4],
            longitude : data_array[i*22+5]
        }
        
        await Info.create(json)
    }
}
shopPhotoInput = async (data) => {
    let data_array = data.split('\n');
    for(let i = 0; i < data_array.length/22; i++)
    {
	let main = await getRandomArbitrary(0, 7);
        let json = [
	{
		main_photo: main,
		photo1: data_array[i*22 + 15],
		photo2: data_array[i*22 + 16],
		photo3: data_array[i*22 + 17],
		photo4: data_array[i*22 + 18],
		photo5: data_array[i*22 + 19],
		photo6: data_array[i*22 + 20],
		photo7: data_array[i*22 + 21],
		photo8: null,
		photo9: null,
		photo10: null
	},
	{
            where: {shopname : data_array[i*22]},
        }
	];

        await Info.update(json[0],json[1]);
    }
}
//shopDataInput(gang, ' (강남)');
//shopDataInput(jongro, ' (종로)');
//shopDataInput(hong, ' (홍대)');
shopPhotoInput(gang);
shopPhotoInput(jongro);
shopPhotoInput(hong);
