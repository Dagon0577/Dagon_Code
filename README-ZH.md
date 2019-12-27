# Dagon_Code
只是一个小实验。

从Windows，Linux获取CPU，主板，MAC地址，虚拟ID（docker，machine-rkt），并生成Dagon_Code。
## 快速开始

运行testCase01，机器信息存储在Info.txt中

## 加密方案设计

(1)对原始信息资源使用SHA1算法进行加密，产生128位散列值;

(2)将步骤(1)产生的固定长度散列值作为Blowfish算法的用户密钥，初始化Blowfish加密算法;

(3)使用初始化完成后的Blowfish算法对原始信息资源的进行加密，加密后即会产生高安全性的密文数据。
