###
 # @Descripttion: 
 # @version: 
 # @Author: miporterg
 # @Date: 2020-11-11 20:11:29
 # @LastEditors: miporterg
 # @LastEditTime: 2020-11-11 20:15:46
### 
cd javaPro
kill -9 $(netstat -nlp | grep :8080 | awk '{print $7}' | awk -F"/" '{ print $1 }')
nohup java -jar ruanjian.jar &