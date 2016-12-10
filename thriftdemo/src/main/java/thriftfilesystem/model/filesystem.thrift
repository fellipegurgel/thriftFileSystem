namespace java thriftclasses
 
struct FakeFile
{
    1:string name,
    2:string creation,
    3:string modification,
    4:i32 version,
    5:binary data,
    6:set<string> children, 
}

service FileSystem {
    string getFile(1:string path);
    string hi();
    string hi2();
    string listChildren(1:string path),
    string addFile(1:string path, 2:binary data),
    string updateFile(1:string path, 2:binary data),
    string deleteFile(1:string path),
    string updateByVersion(1:string path, 2:binary data, 3:i32 version),
    string deleteByVersion(1:string path, 2:i32 version),
    bool checkFile(1:string path),
}
