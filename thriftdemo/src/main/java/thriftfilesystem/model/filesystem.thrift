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
    FakeFile GetFile(1:string path);
    string hi();
    list<string> ListChildren(1:string path),
    bool AddFile(1:string path, 2:binary data),
    FakeFile UpdateFile(1:string path, 2:binary data),
    FakeFile DeleteFile(1:string path),
    FakeFile UpdateVersion(1:string path, 2:binary data, 3:i32 version),
    FakeFile DeleteVersion(1:string path, 2:i32 version),
}
