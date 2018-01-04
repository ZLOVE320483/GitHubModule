# GitHubModule
## 项目说明
    此项目作用主要用于给App Icon增加红色角标和数字，提高App的打开率和日活，针对强迫症用户设计... —— ——！
## 使用方法
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
    dependencies {
            compile 'com.github.ZLOVE320483:GitHubModule:v1.0.1'
    }
    
    // 增加角标
    boolean isSuccess = RedBadgerManager.inst().applyCount(context, badgerCount);
    // 去除角标
    boolean isSuccess = RedBadgerManager.inst().removeCount(this);
