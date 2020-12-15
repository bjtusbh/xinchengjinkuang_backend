###
 # @Author: your name
 # @Date: 2020-11-10 23:05:21
 # @LastEditTime: 2020-11-11 20:37:29
 # @LastEditors: miporterg
 # @Description: In User Settings Edit
 # @FilePath: /Desktop/deploy.sh
### 
Serve="49.234.4.204"
User="root"

echo ${Serve}

echo 'Conncect to server rm old file' 
ssh ${User}@${Serve} "cd javaPro; rm -rf ruanjian.jar;";
echo 'Upload ruanjian.jar'
scp target/ruanjian-0.0.1-SNAPSHOT.jar ${User}@${Serve}:javaPro/ruanjian.jar
echo 'Conncect to server run ruanjian.jar'
# ssh ${User}@${Serve} "";
ssh ${User}@${Serve} sh serve_back.sh;
echo 'DONE!'
