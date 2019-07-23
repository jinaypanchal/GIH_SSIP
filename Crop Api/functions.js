
const fs = require("fs");
function get_crop(city,temp,moi){
city_soil = {Black: ["surat", "vapi", "bhopal", "indore"], Loamy: ["gwalior", "ahmedabad", "gandhinagar", "valsad"], Clayey: ["chennai", "jaipur"], Sandy: ["amritsar", "shimla"], Aluvial : ["merrut", "chandigarh", "kanpur"]}
   for (const key in city_soil) {
       if(city_soil[key].indexOf(city)!==-1){
          return key;
       }
   }
}
module.exports = {
get_crop
}