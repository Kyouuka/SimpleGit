# 项目说明
***
## 一、类和属性
### Commit

用于产生commit对象

* String tree 当前commit的根目录的sha1值

* String parent 前一次commit的sha1值

* String author 作者的名字，邮箱，时间和时区

* String commiter 提交者的信息

* String message 提交的信息
### Branch
用于查看、修改分支信息

* String branchName 当前branch的名字

* Commit commit 当前分支最新提交指向的commit  

### Core
执行核心命令

* String rootDir 当前jit项目的根目录地址

* String branchName 当前所处分支的名字

* Commit commit 当前分支最新的commit对象

### CLI
用于命令行交互

* String command 用户输入的指令

* String parsedCommand 解析后的指令

### FileCreation
创建文件及文件夹


### FileDeletion
删除文件

### FileReader
读取文件内容

### Index
暂存区暂存文件
* LinkedList<String[]>  IndexList  存储文件信息，其中String[] 里面存储了blob的key，blob的文件相对路径（相对于工作目录），以及文件最终修改时间。

## 二、类和方法
### Commit

*  Commit(String treePath, String author, String committer, String message)
  设置commit对象的fmt，path，treePath，parent，author，message。

* Commit(String commitId)
   根据已有的id生成commit对象，用于查找commit历史

* genKey()
   return (String key)
   产生本次commit 的hash值

* isValidCommit()
   return (boolean)
   验证是否是有效地commit，如果本次commit和上次相同，那么commit无效

* logCommitHistory()
   查看当前commit的历史，在Branch类中调用此方法

* getLastCommit()
   return (String parent)
   返回上一次commit的sha1值

* ~~resetCommit(String commitId)~~
   ~~回滚记录到指定的commit~~
   
### Branch

* Branch(String branchName, String commitId)
   根据分支名和commit新建一个分支对象

* Branch(String branchName)
   根据分支名创建分支对象（已存在这样的分支），可以用到在原有分支基础上创建一个新分支

* getName()
   return (String branchName)
   返回当前branch的名称

* update()
   return(String commitId)
   更新commitId

* writeBranch(String commitID)
   修改branch的最新指向的commit

* writeHead(Commit commit)
   修改head的最新指向的commit

* getHistory()
   return (String history)
   返回当前branch的commit history，调用Commit类的logCommitHistory

* ~~reset(String commitId)~~
   ~~回滚历史，会调用writeBranch，writeHead，writeHistory（这里假设回滚以后回滚之后的记录会消失）以及Commit类的resetCommit~~

* ~~checkout()
   切换到当前分支~~
### Core

* jitCommit()
   jit commit命令

* jitCheckout(String branchName)
   切换分支，包括创建新分支

* jitReset(String commitId)
   回滚分支

* jitLog()
   return (String[] commits, int number)
   查看commit history的最后number条，默认50条

* jitBranch()
   return (String branchName)
   查看当前存在的branch

* jitInit(String rootDir)
   根据根目录，在根目录下建立一系列初始文件
   
 ### CLI
 * main(String command)
 接收用户指令
 
 * parseCommand(String command)
 return (String parsedCommand)
解析用户指令

* jitCall(String parsedCommand)
根据解析后的指令调用Core命令
   
### FileCreation
* createFile(String parentPath, String fileName, String text)
在指定目录下创建并写入文件

* createDirectory(String parentPath,String[] paths)
在指定目录下创建文件夹（可以创建多层）

### FileDeletion
* deleteFile(String path)
删除指定文件

* deleteFile(File file)
删除指定文件

### FileReader
* readByBufferReader(String value)
return (Arraylist<String> fileValue)
按行读取文件并放回相应的字符串数组

* readObjectKey(String line)
return (String key)
从line中提取object的Key。line是tree中的一行。另外还有读取fmt，filename的方法。

* readCommitTree(String value)
return (String key)
从commit的value中读取tree的信息

另外还有读取parent，author，committer，msg的方法。待补充。

### Index
* Index(String path)
用index文件路径生成index实例

* insertIndex(File file)
将file内容添加到index中

* clear()
清空index文件中内容

* updateIndex(File file)
根据时间戳来判定是否需要更新File的记录

* isTracked(File file)
return (Boolean track)
判断文件是否已经加入暂存区
