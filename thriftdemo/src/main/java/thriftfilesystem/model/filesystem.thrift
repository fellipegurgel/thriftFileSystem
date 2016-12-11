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
    string hi();
    string hi2();
    string getFile(1:string path);
    string listChildren(1:string path),
    string addFile(1:string path, 2:binary data),
    string updateFile(1:string path, 2:binary data, 3:bool chechVersion, 4:i32 version),
    string deleteFile(1:string path, 2:bool checkVersion, 3:i32 version),
    bool checkFile(1:string path),
    bool addChild(1:string path, 2:string fileName),
    bool deleteChild(1:string path, 2:string fileName),
}
