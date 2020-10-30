const express = require('express');
const router = express.Router();

const env = process.env;
const redis = require('redis');
const client = redis.createClient(env.REDIS_PORT, env.REDIS_HOST);

router.get('/', function(req, res, next) {

  try {
    client.hgetall('recommend', function (err, obj) {
        if(err){
            console.log(err);
            res.send("error "+err);
            return;
        }
        
        if(obj)
        {
          res.json({
	    hot: JSON.parse(obj.hot),
	    food: JSON.parse(obj.food),  
            courses: JSON.parse(obj.courses)
          });
        }
        else
        {
          res.json({
            code: 500,
            message: "no data in redis"
          });
        }
    });
  }
  catch(err) {
    console.log(err);
    res.json({
      code: 500,
      message: "redis read error"
    });
  }

});

module.exports = router;
