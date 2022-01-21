# open-car-share
Open source solution to find transport within or between cities. [work in progress, open for PRs, check issues tab]

As a driver I create trip at the specific date and time, e.g. "Kazan - Moscow 7:00 21.11.2022" 

As a driver I can pick up two people on this trip

As a user I can view all trips on specific date and time according my trip location criterias

As a user I can book a trip

As a user I can confirm trip by phone call with driver

# deployment
Server

    Setup environment

console: sudo apt-get install nodejs

console: sudo apt update

console: sudo apt install mysql-server [for more details: https://www.digitalocean.com/community/tutorials/how-to-install-mysql-on-ubuntu-20-04]

console: npm install

console: mysql -u -p

console: CREATE DATABASE db_car_share_schema;

console: exit

console: cd path/to/backend

console: mysql -u -p db_car_share_schema < db_car_share_schema.sql

Update config.js with webhost ip address, dbhost ip address, database user name and password

Next steps I did over mysql workbench and console, but it should be possible use console only:
a. Setup MYSQL to support UTF8 or utf8mb4 encoding (by default latin1 enconding doesn't support cyrillic characters) 
b. Check character_set_client and character_set_result enconding are the same by executing this command in mysql console:

show variables like 'character_set%';

+--------------------------+----------------------------+
| Variable_name            | Value                      |
+--------------------------+----------------------------+
| character_set_client     | utf8                       |
| character_set_connection | utf8                       |
| character_set_database   | latin1                     |
| character_set_filesystem | binary                     |
| character_set_results    | utf8                       |
| character_set_server     | latin1                     |
| character_set_system     | utf8                       |
| character_sets_dir       | /usr/share/mysql/charsets/ |
+--------------------------+----------------------------+

2. Run and operate server:
console: sudo mysql restart

console: sudo mysql -u -p

console: USE <db_name>;

console: exit

console: cd <path/to/backend>

console: node app.js

3. On the Android client:

Update config.xml with server actual ip address

