namespace java thriftclasses
 
struct FakeFile
{
    1:string creation,
    2:string modification,
    3:i32 version,
    4:binary data,
    5:set<string> children, 
}

service FileSystem {
    string getFile(1:string path);
    string hi();
    string listChildren(1:string path),
    string addFile(1:string path, 2:binary data),
    string updateFile(1:string path, 2:binary data),
    string deleteFile(1:string path),
    string updateByVersion(1:string path, 2:binary data, 3:i32 version),
    string deleteByVersion(1:string path, 2:i32 version),
}
