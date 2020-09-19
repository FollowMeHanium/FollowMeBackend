var express = require('express');
var router = express.Router();
var {User} = require('../models');
var bcrypt = require('bcryptjs');
var crypto = require('crypto'); 
const jwt = require('jsonwebtoken');
var client = require('../redis_config');
var passport = require('passport');

var isEmpty = function (value) {
  if (value == "" || value == null || value == undefined || (value != null && typeof value == "object" && !Object.keys(value).length)) {
    return true
  } else {
    return false
  }
};

router.post('/login', function(req, res, next) {
  try{
    passport.authenticate('local', {session : false}, async (authError, user, info) => {
      if(authError){
        console.log(authError);
        console.error(authError);
        return next(authError);
      }

      if (!user) {
        console.log(info.message);
        return res.json({
          code:400,
          message:info.message
        });
      }
      var now = new Date();
      var age = now.getFullYear()-user.birthdayYear;
      console.log(now + " " + age);
      try{
        const token = jwt.sign({
          id : user.id,
          nickname : user.nickname,
          gender : user.gender,
          age : age,
          status : user.status,
        },
        process.env.JWT_SECRET,
        {
            expiresIn : '60m',
            issuer : 'comeOn',
        });
        
        const refreshToken = jwt.sign({
          id : user.id,
          },
          process.env.JWT_SECRET,
          {
              expiresIn : '1440m',
              issuer : 'comeOn',
          });
          
        client.set(user.id,refreshToken);

          return res.status(200).json({
            code : 200,
            message : '토큰이 발급되었습니다.',
            token,
            refreshToken,
          }).send();
      }
      catch(err){
        console.log(err);
        return res.status(400).json({
          code:400,
          message:"에러입니다."
        }).send();
      }
    })(req, res, next); 
  }
  catch(err){
    console.log(err);
    res.send("에러입니다.");
  }
});

router.post('/join',async function(req, res, next) {
  var {email,nickname,password,phone_num,gender,birthdayYear,birthdayMonth,birthdayDay} = req.body;
  console.log(birthdayYear);
  await User.findOne({where:{
    email:email
  }})
  .then(async result=>{
    if(isEmpty(result)!=false){
      var salt = Math.round((new Date().valueOf() * Math.random())) + "";
      var hashpassword = crypto.createHash('sha512').update(password+salt).digest('base64');
      await User.create({
        email:email,
        nickname:nickname,
        password:hashpassword,
        salt:salt,
        phone_num:phone_num,
        gender:gender,
        birthdayYear:birthdayYear,
        birthdayMonth:birthdayMonth,
        birthdayDay:birthdayDay
      })
      .then(result=>{
        res.json({
          code:200,
          message:"Join Success"
        });
      })
    }else{
      return res.status(400).send("이미 가입된 메일입니다.");
    }
  });
});

module.exports = router;
