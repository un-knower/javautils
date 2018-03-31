CREATE TABLE `NewTable` (
`id`  int(20) NOT NULL ,
`varcharType`  varchar(255) CHARACTER SET gb2312 COLLATE gb2312_chinese_ci NULL DEFAULT NULL ,
`timeType`  time NULL DEFAULT NULL ,
`timestampType`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '在创建新记录和修改现有记录的时候都对这个数据列刷新' ,
`doubleType`  double NULL DEFAULT NULL ,
`decimalType`  decimal(10,4) NULL DEFAULT NULL ,
`textType`  text CHARACTER SET gb2312 COLLATE gb2312_chinese_ci NULL ,
`blobType`  blob NULL ,
`yearType`  year NULL DEFAULT NULL ,
`enumType`  enum('NONE','3','2','1') CHARACTER SET gb2312 COLLATE gb2312_chinese_ci NULL DEFAULT NULL ,
`setType`  set('北京','上海','广州','深圳','杭州','大理','西藏') CHARACTER SET gb2312 COLLATE gb2312_chinese_ci NULL DEFAULT NULL ,
`tinyintType`  tinyint(4) NULL DEFAULT NULL ,
`smallintType`  smallint(6) NULL DEFAULT NULL ,
`mediumintType`  mediumint(9) NULL DEFAULT NULL ,
`integerType`  int(11) NULL DEFAULT NULL ,
`bigintType`  bigint(20) NULL DEFAULT NULL ,
`bitType`  bit(1) NULL DEFAULT NULL ,
`realType`  double NULL DEFAULT NULL ,
`floatType`  float NULL DEFAULT NULL ,
`numericType`  decimal(10,4) NULL DEFAULT NULL ,
`charType`  char(255) CHARACTER SET gb2312 COLLATE gb2312_chinese_ci NULL DEFAULT NULL ,
`tinyblobType`  tinyblob NULL ,
`mediumblobType`  mediumblob NULL ,
`longtextType`  longtext CHARACTER SET gb2312 COLLATE gb2312_chinese_ci NULL ,
`binaryType`  binary(255) NULL DEFAULT NULL ,
`varbinaryType`  varbinary(255) NULL DEFAULT NULL ,
`pointType`  point NULL DEFAULT NULL ,
`linestringType`  linestring NULL DEFAULT NULL ,
`polygonType`  polygon NULL DEFAULT NULL ,
`geometryType`  geometry NULL DEFAULT NULL ,
`multipointType`  multipoint NULL DEFAULT NULL ,
`multilinestringType`  multilinestring NULL DEFAULT NULL ,
`multipolygonType`  multipolygon NULL DEFAULT NULL ,
`geometrycollectionType`  geometrycollection NULL DEFAULT NULL ,
PRIMARY KEY (`id`),
INDEX `id` USING BTREE (`id`) 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=gb2312 COLLATE=gb2312_chinese_ci
ROW_FORMAT=COMPACT

;