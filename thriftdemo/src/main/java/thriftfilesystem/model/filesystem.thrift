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
    string getFile(1:list<string> path);
    string hi();
    string listChildren(1:list<string> path),
    string addFile(1:list<string> path, 2:binary data),
    string updateFile(1:list<string> path, 2:binary data),
    string deleteFile(1:list<string> path),
    string updateByVersion(1:list<string> path, 2:binary data, 3:i32 version),
    string deleteByVersion(1:list<string> path, 2:i32 version),
}
