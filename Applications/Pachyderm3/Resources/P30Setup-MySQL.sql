set names utf8;
set FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  TABLE STRUCTURE FOR `APPERSON`
-- ----------------------------
-- DROP TABLE IF EXISTS `APPERSON`;
create table `APPERSON` (
    `ADDRESS` blob,
    `AIM_INSTANT` blob,
    `BIRTHDAY` datetime default null,
    `CUSTOM_PROPERTIES` blob,
    `DATE_CREATED` datetime default null,
    `DATE_MODIFIED` datetime default null,
    `DEPARTMENT` varchar(255) default null,
    `EMAIL` blob,
    `FIRSTNAME_PHONETIC` varchar(255) default null,
    `FIRSTNAME` varchar(255) default null,
    `HOMEPAGE` varchar(255) default null,
    `ICQ_INSTANT` blob,
    `IMAGE_DATA` blob,
    `JABBER_INSTANT` blob,
    `JOBTITLE` varchar(255) default null,
    `LASTNAME_PHONETIC` varchar(255) default null,
    `LASTNAME` varchar(255) default null,
    `MAIDENNAME` varchar(255) default null,
    `MIDDLENAME_PHONETIC` varchar(255) default null,
    `MIDDLENAME` varchar(255) default null,
    `MSN_INSTANT` blob,
    `MULTIMAIL` varchar(255) default null,
    `NICKNAME` varchar(255) default null,
    `NOTE` varchar(255) default null,
    `ORGANIZATION` varchar(255) default null,
    `OTHERDATES` blob,
    `PERSON_FLAGS` int(11) default null,
    `PERSON_ID` int(11) not null default '0',
    `PHONE` blob,
    `RELATEDNAMES` blob,
    `SUFFIX` varchar(63) default null,
    `TITLE` varchar(63) default null,
    `YAHOO_INSTANT` blob,
    primary key (`PERSON_ID`)
)  engine=innodb default charset=utf8;

-- ----------------------------
--  RECORDS OF `APPERSON`
-- ----------------------------
begin;
insert into `APPERSON` values (null, null, null, null, '2013-01-01 00:00:00', null, null, null, null, 'Pachyderm', null, null, null, null, null, null, 'Administrator', null, null, null, null, null, null, null, null, null, null, '1', null, null, null, null, null);
commit;

-- ----------------------------
--  TABLE STRUCTURE FOR `APGROUP`
-- ----------------------------
-- DROP TABLE IF EXISTS `APGROUP`;
create table `APGROUP` (
    `CUSTOM_PROPERTIES` blob,
    `GROUP_ID` int(11) not null default '0',
    `NAME` varchar(255) default null,
    `NOTE` varchar(255) default null,
    primary key (`GROUP_ID`)
)  engine=innodb default charset=utf8;

-- ----------------------------
--  RECORDS OF `APGROUP`
-- ----------------------------
begin;
insert into `APGROUP` values (null, '1', 'Admin', 'Administrators');
commit;

-- ----------------------------
--  TABLE STRUCTURE FOR `AUTHMAP`
-- ----------------------------
-- DROP TABLE IF EXISTS `AUTHMAP`;
create table `AUTHMAP` (
    `EXTERNAL_ID` varchar(255) default null,
    `EXTERNAL_REALM` varchar(255) default null,
    `MAP_ID` int(11) not null default '0',
    `PERSON_ID` int(11) default null,
    primary key (`MAP_ID`)
)  engine=innodb default charset=utf8;

-- ----------------------------
--  RECORDS OF `AUTHMAP`
-- ----------------------------
begin;
insert into `AUTHMAP` values ('administrator@pachyderm', 'pachyderm', '1', '1');
commit;

-- ----------------------------
--  TABLE STRUCTURE FOR `AUTHRECORD`
-- ----------------------------
-- DROP TABLE IF EXISTS `AUTHRECORD`;
create table `AUTHRECORD` (
    `PASSWORD` blob,
    `REALM` varchar(127) not null default '',
    `USERNAME` varchar(127) not null default '',
    `TEMP_PASSWORD` blob,
    primary key (`USERNAME` , `REALM`)
)  engine=innodb default charset=utf8;

-- ----------------------------
--  RECORDS OF `AUTHRECORD`
-- ----------------------------
begin;
insert into `AUTHRECORD` values (0x6ecc5a549d8049ea3e176286fa11cabf, 'pachyderm', 'administrator', null);
commit;

-- ----------------------------
--  TABLE STRUCTURE FOR `ATTRIBUTE`
-- ----------------------------
-- DROP TABLE IF EXISTS `ATTRIBUTE`;
create table `ATTRIBUTE` (
    `VALUE` blob,
    `IDENTIFIER` varchar(191) not null default '',
    `ATTRKEY` varchar(127) not null default '',
    primary key (`IDENTIFIER` , `ATTRKEY`)
)  engine=innodb default charset=utf8;

-- ----------------------------
--  TABLE STRUCTURE FOR `COMPONENT_2_1`
-- ----------------------------
-- DROP TABLE IF EXISTS `COMPONENT_2_1`;
create table `COMPONENT_2_1` (
    `BINDING_VALUES` blob,
    `COMPONENT_DESCRIPTION_CLASS` varchar(255) default null,
    `DATE_CREATED` datetime default null,
    `DATE_MODIFIED` datetime default null,
    `IDENTIIFER` varchar(127) default null,
    `LOCALIZED_DESCRIPTION` mediumtext,
    `METADATA` mediumtext,
    `ID` int(11) not null default '0',
    `TITLE` varchar(255) default null,
    `PARENT_COMPONENT_ID` int(11) default null,
    primary key (`ID`)
)  engine=innodb default charset=utf8;

-- ----------------------------
--  TABLE STRUCTURE FOR `GROUPGROUPJOIN`
-- ----------------------------
-- DROP TABLE IF EXISTS `GROUPGROUPJOIN`;
create table `GROUPGROUPJOIN` (
    `PARENT_ID` int(11) not null default '0',
    `SUB_ID` int(11) not null default '0',
    primary key (`PARENT_ID` , `SUB_ID`)
)  engine=innodb default charset=utf8;

-- ----------------------------
--  TABLE STRUCTURE FOR `GROUPPERSONJOIN`
-- ----------------------------
-- DROP TABLE IF EXISTS `GROUPPERSONJOIN`;
create table `GROUPPERSONJOIN` (
    `GROUP_ID` int(11) not null default '0',
    `PERSON_ID` int(11) not null default '0',
    primary key (`GROUP_ID` , `PERSON_ID`)
)  engine=innodb default charset=utf8;

-- ----------------------------
--  RECORDS OF `GROUPPERSONJOIN`
-- ----------------------------
begin;
insert into `GROUPPERSONJOIN` values ('1', '1');
commit;

-- ----------------------------
--  TABLE STRUCTURE FOR `INVITATION`
-- ----------------------------
-- DROP TABLE IF EXISTS `invitation`;
create table `invitation` (
    `ISACTIVATED` int(11) default null,
    `ACTIVATEDBY` int(11) default null,
    `CREATEDBY` int(11) default null,
    `DATECREATED` timestamp not null default current_timestamp on update current_timestamp,
    `ID` int(11) not null default '0',
    `KEYSTRING` varchar(255) default null,
    primary key (`ID`)
)  engine=innodb default charset=utf8;

-- ----------------------------
--  TABLE STRUCTURE FOR `metadata2`
-- ----------------------------
-- DROP TABLE IF EXISTS `metadata2`;
create table `metadata2` (
    `dccontributor` text,
    `dccoverage` text,
    `dccreator` text,
    `dcdate` text,
    `dcdescription` text,
    `dcformat` text,
    `dcidentifier` int(11) not null auto_increment,
    `dclanguage` text,
    `dcidentifieruri` text,
    `dcidentifierurl` text,
    `dcpublisher` text,
    `dcrelation` text,
    `dcrights` tinytext,
    `dcsource` text,
    `dcsubject` text,
    `dctitle` text,
    `dctype` text,
    `dctermsaccessrights` text,
    `dctermsalternative` text,
    `dctermsaudience` text,
    `dctermscreated` text,
    `dctermsdateaccepted` text,
    `dctermsdatecopyrighted` text,
    `dctermsdatesubmitted` datetime default null,
    `dctermseducationlevel` text,
    `dctermsextent` text,
    `dctermsinstructionalmethod` text,
    `dctermsissued` text,
    `dctermslicense` text,
    `dctermsmediator` text,
    `dctermsmedium` text,
    `dctermsmodified` datetime default null,
    `dctermsprovenance` text,
    `dctermsrightsholder` text,
    `dctermsspatial` text,
    `dctermstemporal` text,
    `dctermsvalid` text,
    `medialabel` text,
    `accalttext` text,
    `acclongdesc` text,
    `acctranscript` text,
    `accsynccaption` text,
    primary key (`dcidentifier`)
)  engine=innodb auto_increment=26 default charset=utf8;

-- ----------------------------
--  TABLE STRUCTURE FOR `PRESENTATION_2_1`
-- ----------------------------
-- DROP TABLE IF EXISTS `PRESENTATION_2_1`;
create table `PRESENTATION_2_1` (
    `DATE_CREATED` datetime default null,
    `DATE_MODIFIED` datetime default null,
    `IDENTIIFER` varchar(127) default null,
    `LOCALIZED_DESCRIPTION` mediumtext,
    `METADATA` mediumtext,
    `ID` int(11) not null default '0',
    `PRIMARY_SCREEN_ID` int(11) default null,
    `TITLE` varchar(255) default null,
    `AUTHOR` varchar(255) default null,
    primary key (`ID`)
)  engine=innodb default charset=utf8;

-- ----------------------------
--  TABLE STRUCTURE FOR `SCREEN_2_1`
-- ----------------------------
-- DROP TABLE IF EXISTS `SCREEN_2_1`;
create table `SCREEN_2_1` (
    `DATE_CREATED` datetime default null,
    `DATE_MODIFIED` datetime default null,
    `IDENTIIFER` varchar(127) default null,
    `LOCALIZED_DESCRIPTION` mediumtext,
    `METADATA` mediumtext,
    `ID` int(11) not null default '0',
    `PRESENTATION_ID` int(11) default null,
    `ROOT_COMPONENT_ID` int(11) default null,
    `TITLE` varchar(255) default null,
    primary key (`ID`)
)  engine=innodb default charset=utf8;

-- ----------------------------
--  TABLE STRUCTURE FOR `EO_PK_TABLE`
-- ----------------------------
-- DROP TABLE IF EXISTS `EO_PK_TABLE`;
create table `eo_pk_table` (
    `NAME` char(40) not null default '',
    `PK` int(11) default null,
    primary key (`NAME`)
)  engine=innodb default charset=utf8;

-- ----------------------------
--  RECORDS OF `EO_PK_TABLE`
-- ----------------------------
begin;
insert into `eo_pk_table` values ('APPERSON', '100'),
                                 ('APGROUP', '100'),
                                 ('AUTHMAP', '100'),
                                 ('AUTHRECORD', '100'),
                                 ('PRESENTATION', '100'),
                                 ('SCREEN', '100'),
                                 ('COMPONENT', '100'),
                                 ('invitation', '1'),
                                 ('metadata2', '1'),
                                 ('PRESENTATION_2_1', '100'),
                                 ('SCREEN_2_1', '100'),
                                 ('COMPONENT_2_1', '100');
commit;

set FOREIGN_KEY_CHECKS = 1;
