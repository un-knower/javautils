# rsa_aes_md5  加密算法

## RSA(SHA1withRSA/pem私钥0/crt证书公钥) + AES(256/AES/CBC/PKCS5Padding)

## 公私钥生成：
```bash
openssl genrsa -out rsa_private_key.pem 2048
openssl pkcs8 -topk8 -inform PEM -in rsa_private_key.pem -outform PEM -nocrypt -out private_key.pem
openssl rsa -in rsa_private_key.pem -pubout -out rsa_public_key.pem
openssl req  -new -x509  -days  2048 -key  rsa_private_key.pem  -out rsa_public_key.crt
```

```text
非对称加密算法：RSA
加密模式：SHA1withRSA
PS：解析私钥.pem文件时需对字符串进行Base64解码

对称加密算法：AES
密钥Key随机生成位数：256
加密模式：AES/CBC/PKCS5Padding
```


