// var mysql = require('mysql');
// var express = require('express');
// var app = express();
// app.listen(3000,function(){
// 	console.log('Server started');
// });

// var con = mysql.createConnection({
//   host: "localhost",
//   user: "root",
//   password: null,
//   database: "food"
// });
// con.connect();


// app.get('/api',function(req,res){
// 	  // if (err) throw err;
//   con.query("SELECT * FROM foods", function (err, result, fields) {
// 	if (err) throw err;
//   	var objs = [];
// 	for (var i = 0; i < result.length; i++)
// 	{
//     	objs.push({id: result[i].id, ten: result[i].ten, hinh: result[i].hinh});
// 	}
// 	res.send(result);
//     console.log(JSON.stringify(objs));
    
//   });
// });
//----------------------------------
// var mysql = require('mysql');
// var express = require('express');
// var app = express();
// app.listen(3000,function(){
// 	console.log('Server started');
// });

// var con = mysql.createConnection({
//   host: "localhost",
//   user: "root",
//   password: null,
//   database: "food"
// });
// con.connect();


// app.get('/api/:minID',function(req,res){
// 	  // if (err) throw err;
//   con.query("SELECT * FROM foods", function (err, result, fields) {
// 	if (err) throw err;
//   	var objs = [];
//   	var minID = parseInt(req.params.minID) * 5;
// 	var maxID = minID + 5;	
// 	for (var i = minID; i < maxID; i++)
// 	{	

//     	objs.push({id: result[i].id, ten: result[i].ten, hinh: result[i].hinh}); 	

// 	}
// 	res.send(objs);
//     console.log(JSON.stringify(objs));
    
//   });
// });
//------------------------------------
var mysql = require('mysql');
var express = require('express');
var app = express();
app.listen(3000,function(){
	console.log('Server started');
});

var con = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: null,
  database: "food"
});
var objs = [];
con.connect(function(err) {
  if (err) throw err;
  con.query("SELECT * FROM foods", function (err, result, fields) {
	if (err) throw err;
  	
	for (var i = 0; i < result.length; i++)
	{
    	objs.push({id: result[i].id, ten: result[i].ten, hinh: result[i].hinh});
	}
	
    // console.log(JSON.stringify(objs));

  });
});

app.get('/api/:minID',function(req,res){

  	var minID = parseInt(req.params.minID) * 5;
	var maxID = minID + 5;	
	var result = objs.filter(function(e,i){
			return (i>=minID) && (i< maxID);
		});
		res.send(result);
    console.log(JSON.stringify(result));
    
});
