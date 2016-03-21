create table ORACLEDB
(
  id                             NUMBER not null,
  numbertype                     NUMBER,
   longtype                       LONG,
  varchar2type                   VARCHAR2(200),
  nvarchar2type                  NVARCHAR2(200),
  binary_floattype               BINARY_FLOAT,
  binary_doubletype              BINARY_DOUBLE,
  chartype                       CHAR(50),
  datetype                       DATE,
  intervaldaytosecondtype        INTERVAL DAY(2) TO SECOND(6),
  intervalyeartomonthtype        INTERVAL YEAR(2) TO MONTH,
  rawtype                        RAW(200),
  timestamptype                  TIMESTAMP(6),
  timestampwithlocaltimezonetype TIMESTAMP(6) WITH LOCAL TIME ZONE,
  timestampwithtimezonetype      TIMESTAMP(6) WITH TIME ZONE,
  clobtype                       CLOB,
  nclobtype                      NCLOB,
  blobtype                       BLOB
)