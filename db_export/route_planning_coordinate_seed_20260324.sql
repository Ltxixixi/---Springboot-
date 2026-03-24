UPDATE `spot` SET `latitude` = 39.916345, `longitude` = 116.397155 WHERE `id` = 3001001;
UPDATE `spot` SET `latitude` = 39.999629, `longitude` = 116.275451 WHERE `id` = 3001002;
UPDATE `spot` SET `latitude` = 40.356389, `longitude` = 116.020556 WHERE `id` = 3001003;
UPDATE `spot` SET `latitude` = 39.882200, `longitude` = 116.406600 WHERE `id` = 3001004;
UPDATE `spot` SET `latitude` = 39.941000, `longitude` = 116.403700 WHERE `id` = 3001005;
UPDATE `spot` SET `latitude` = 39.942500, `longitude` = 116.385300 WHERE `id` = 3001006;

UPDATE `spot` SET `latitude` = 30.247100, `longitude` = 120.155100 WHERE `id` = 3002001;
UPDATE `spot` SET `latitude` = 30.242800, `longitude` = 120.101500 WHERE `id` = 3002002;
UPDATE `spot` SET `latitude` = 30.272100, `longitude` = 120.063900 WHERE `id` = 3002003;
UPDATE `spot` SET `latitude` = 30.242600, `longitude` = 120.171700 WHERE `id` = 3002004;
UPDATE `spot` SET `latitude` = 29.594600, `longitude` = 118.993100 WHERE `id` = 3002005;

UPDATE `spot` SET `latitude` = 34.384900, `longitude` = 109.273200 WHERE `id` = 3003001;
UPDATE `spot` SET `latitude` = 34.266300, `longitude` = 108.943400 WHERE `id` = 3003002;
UPDATE `spot` SET `latitude` = 34.218600, `longitude` = 108.962100 WHERE `id` = 3003003;
UPDATE `spot` SET `latitude` = 34.263700, `longitude` = 108.947100 WHERE `id` = 3003004;
UPDATE `spot` SET `latitude` = 34.361900, `longitude` = 109.214500 WHERE `id` = 3003005;

UPDATE `spot` SET `latitude` = 30.667200, `longitude` = 104.055500 WHERE `id` = 3004001;
UPDATE `spot` SET `latitude` = 30.650600, `longitude` = 104.043800 WHERE `id` = 3004002;
UPDATE `spot` SET `latitude` = 30.739900, `longitude` = 104.152500 WHERE `id` = 3004003;
UPDATE `spot` SET `latitude` = 30.904600, `longitude` = 103.567800 WHERE `id` = 3004004;
UPDATE `spot` SET `latitude` = 30.998400, `longitude` = 103.619900 WHERE `id` = 3004005;
UPDATE `spot` SET `latitude` = 30.658700, `longitude` = 104.081700 WHERE `id` = 3004006;

UPDATE `spot` SET `latitude` = 31.240000, `longitude` = 121.490000 WHERE `id` = 3005001;
UPDATE `spot` SET `latitude` = 31.239700, `longitude` = 121.499800 WHERE `id` = 3005002;
UPDATE `spot` SET `latitude` = 31.227200, `longitude` = 121.492000 WHERE `id` = 3005003;
UPDATE `spot` SET `latitude` = 31.234500, `longitude` = 121.475300 WHERE `id` = 3005004;
UPDATE `spot` SET `latitude` = 31.144300, `longitude` = 121.657200 WHERE `id` = 3005005;

UPDATE `spot` SET `latitude` = 32.023800, `longitude` = 118.792700 WHERE `id` = 3006001;
UPDATE `spot` SET `latitude` = 32.060300, `longitude` = 118.850700 WHERE `id` = 3006002;
UPDATE `spot` SET `latitude` = 32.075400, `longitude` = 118.792300 WHERE `id` = 3006003;
UPDATE `spot` SET `latitude` = 32.040100, `longitude` = 118.839400 WHERE `id` = 3006004;
UPDATE `spot` SET `latitude` = 32.015400, `longitude` = 118.798700 WHERE `id` = 3006005;

UPDATE `spot` SET `latitude` = 29.563000, `longitude` = 106.580000 WHERE `id` = 3007001;
UPDATE `spot` SET `latitude` = 29.558300, `longitude` = 106.577100 WHERE `id` = 3007002;
UPDATE `spot` SET `latitude` = 29.554100, `longitude` = 106.570300 WHERE `id` = 3007003;
UPDATE `spot` SET `latitude` = 29.581400, `longitude` = 106.446700 WHERE `id` = 3007004;
UPDATE `spot` SET `latitude` = 29.401300, `longitude` = 107.756700 WHERE `id` = 3007005;

UPDATE `spot` SET `latitude` = 31.326800, `longitude` = 120.631000 WHERE `id` = 3008001;
UPDATE `spot` SET `latitude` = 31.317300, `longitude` = 120.632500 WHERE `id` = 3008002;
UPDATE `spot` SET `latitude` = 31.302100, `longitude` = 120.554500 WHERE `id` = 3008003;
UPDATE `spot` SET `latitude` = 31.117800, `longitude` = 120.844100 WHERE `id` = 3008004;
UPDATE `spot` SET `latitude` = 31.321600, `longitude` = 120.734600 WHERE `id` = 3008005;

UPDATE `spot` SET `latitude` = 25.290300, `longitude` = 110.295000 WHERE `id` = 3009001;
UPDATE `spot` SET `latitude` = 25.273600, `longitude` = 110.292400 WHERE `id` = 3009002;
UPDATE `spot` SET `latitude` = 24.778300, `longitude` = 110.496600 WHERE `id` = 3009003;
UPDATE `spot` SET `latitude` = 25.754000, `longitude` = 110.011000 WHERE `id` = 3009004;
UPDATE `spot` SET `latitude` = 25.279000, `longitude` = 110.290500 WHERE `id` = 3009005;

UPDATE `spot`
SET `visitDurationMinutes` = 480, `openTime` = '08:30', `closeTime` = '21:30'
WHERE `id` = 3005005;

UPDATE `spot`
SET `visitDurationMinutes` = 240, `openTime` = '07:30', `closeTime` = '18:00'
WHERE `id` IN (3001003, 3004004, 3004005, 3007005, 3009004);

UPDATE `spot`
SET `visitDurationMinutes` = 180, `openTime` = '08:00', `closeTime` = '18:00'
WHERE `id` IN (3001001, 3001002, 3001004, 3003001, 3003005, 3006002, 3006004, 3008001);

UPDATE `spot`
SET `visitDurationMinutes` = 150, `openTime` = '10:00', `closeTime` = '22:00'
WHERE `id` IN (3001006, 3002004, 3003003, 3003004, 3004001, 3004002, 3004006, 3005001, 3005004, 3006001, 3006005, 3007001, 3007002, 3007003, 3007004, 3008002, 3008004, 3008005, 3009003, 3009005);
