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
        var expiresTime;
        if(user.status==0){
          expiresTime='60m';
        }else{
          expiresTime='1440m'
        }
        const token = jwt.sign({
          user_id : user.id,
          nickname : user.nickname,
          gender : user.gender,
          age : age,
          status : user.status,
        },
        process.env.JWT_SECRET,
        {
            expiresIn : expiresTime,
            issuer : 'comeOn',
        });
        
        const refreshtoken = jwt.sign({
          user_id : user.id,
          },
          process.env.JWT_SECRET,
          {
              expiresIn : '1440m',
              issuer : 'comeOn',
          });
          
          client.set(user.id,refreshtoken,'EX',86400);//redis에 refreshtoken 저장
          return res.status(200).json({
            code : 200,
            message : '토큰이 발급되었습니다.',
            token,
            refreshtoken,
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

router.post('/checkNickname',function(req,res,next){
  var {nickname}=req.body;

  User.findOne({where:{
    nickname:nickname
  }})
  .then(result=>{
    if(isEmpty(result)!=false){
      res.json({
        code:200,
        message:"사용가능한 닉네임입니다."
      })
    }else{
      res.json({
        code:400,
        message:"이미 사용중인 닉네임입니다."
      })
    }
  })
});

router.post('/checkEmail',function(req,res,next){
  var {email}=req.body;

  User.findOne({where:{
    email:email
  }})
  .then(result=>{
    if(isEmpty(result)!=false){
      res.json({
        code:200,
        message:"사용가능한 이메일입니다."
      })
    }else{
      res.json({
        code:400,
        message:"이미 가입된 이메일입니다."
      })
    }
  })
});

router.post('/getNewToken',function(req,res,next){
  var authorization=req.headers.authorization;
  var refreshtoken = req.headers.refreshtoken;
  var tokenValue=jwt.decode(authorization);
  var userId = tokenValue.user_id;
  client.get(userId,function(err,result){
    if(isEmpty(result)){
      res.json({
        code:400,
        message:"토큰이 만료되었습니다. 재 로그인 해주세요."
      });
    }else if(result==refreshtoken){
      var expiresTime;
      if(tokenValue.status==0){
        expiresTime='60m';
      }else{
        expiresTime='1440m'
      }
      const token = jwt.sign({
        user_id : tokenValue.user_id,
        nickname : tokenValue.nickname,
        gender : tokenValue.gender,
        age : tokenValue.age,
        status : tokenValue.status,
      },
      process.env.JWT_SECRET,
      {
        expiresIn : expiresTime,
        issuer : 'comeOn',
      });
      
      res.status(200).json({
        code : 200,
        message : '토큰이 발급되었습니다.',
        token,
      }).send();  
    }else{
      res.json({
        code:500,
        message:"토큰이 변조되었습니다. 재 로그인 해주세요."
      });
    }
  })
});


module.exports = router;
