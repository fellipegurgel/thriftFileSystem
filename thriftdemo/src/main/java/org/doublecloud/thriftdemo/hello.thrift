namespace java org.doublecloud.thriftdemo
 
struct TPage
{
    1:i64 creation,
    2:i64 modification,
    3:i32 version,
    4:binary data,
}

service Hello {
  TPage GetFile(1:string path);
  string hi();
}
