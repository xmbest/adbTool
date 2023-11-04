package entity

/**
 * model 代号
 * brand 手机品牌
 * device 设备名称
 * serialNo 设备序列号
 * systemVersion 系统版本
 * androidVersion 安卓版本
 * density 分辨率
 * memory 手机运存
 * storage 存储空间
 * cpu 处理器
 * core 核心数
 *
 */
data class DeviceInfo(var model:String = "", var brand:String = "", var device:String = "",
                      var serialNo:String = "", var density:String = "", var systemVersion:String = "",
                      var androidVersion:String = "", var memory:String = "", var storage:String = "", var cpu:String = "",var core:String = "")
