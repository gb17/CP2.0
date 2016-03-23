DROP TABLE IF EXISTS TBINC;
CREATE TABLE TBINC(REPCODE TEXT,KEY_COL TEXT);

insert into TBINC values ('H0001','121524');DELETE FROM TBPARTY WHERE EXISTS(SELECT 1 FROM TBINC A WHERE A.KEY_COL=TBPARTY.KEY_COL);
DROP TABLE IF EXISTS TBTBC;
CREATE TABLE TBTBC(_id integer primary key autoincrement,TAG  TEXT,REC_COUNT  TEXT,MOB_COUNT  TEXT,KEY_COL  TEXT,OPTRN  TEXT);

insert into TBTBC( TAG,REC_COUNT,MOB_COUNT,KEY_COL,OPTRN) values 
('PARTY','98','','PARTY','N');