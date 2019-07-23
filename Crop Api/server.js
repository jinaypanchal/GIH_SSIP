const express = require("express");
const csv = require("csvtojson");
const { get_crop } = require("./functions");
const bodyParser = require("body-parser");
const app = express();

const csvFilePath = "data.csv";

app.use(bodyParser.json());
app.get("/", function(req, res) {
  res.send("please go to /crop");
});

app.post("/crop", function(req, res) {
  var { city, temp, moisture } = req.body;
  temp = parseInt(temp);
  moisture = parseInt(moisture);
  var pre = get_crop(city, temp, moisture);
  console.log(pre);
  csv()
    .fromFile(csvFilePath)
    .then(jsonObj => {
      for (var k in jsonObj) {
        console.log(k);
        if (
          pre === jsonObj[k].soil &&
          (temp >= parseInt(jsonObj[k].min_temp) &&
            temp <= parseInt(jsonObj[k].max_temp)) &&
          (moisture >= parseInt(jsonObj[k].min_moisture) &&
            moisture <= parseInt(jsonObj[k].max_moisture))
        ) {
          return res.send({ crop: jsonObj[k]["crop"] });
        }
      }
      return res.send({ error: "something happened wrong" });
    });
});

app.listen(process.env.PORT || 3000, () => {
  console.log("now it is running" + (process.env.PORT || 3000));
});
