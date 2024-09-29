## Zebra Printer - Bluetooth 印刷デモ用のソースコード(v2.00)

Update:2024/09/29

### 概要

- Bluetooth 接続経由のラベル印刷パフォーマンスをデモするためのツール。
- Android用のBluetooth 接続経由のラベル印刷アプリを開発する方向けにソースコードを提供。

</br>
下記リンクの関連記事です。
https://github.com/shimauma-giken/Zebra-Printer_Android-Demo-Print-Apk-via-Bluetooth-Classic

</br>

### テスト済み環境

- ET4x/A11
- TC21/A11
- EC5x/A11
- ZQ3+, ZQ5, ZQ6,

</br>

### 実装機能

| ボタン          | 機能                                                                                                                             |
| --------------- | -------------------------------------------------------------------------------------------------------------------------------- |
| Open BT         | Bluetooth 接続を開く                                                                                                             |
| Close BT        | Bluetooth 接続を閉じる                                                                                                           |
| Send ZPL        | Bluetooth経由でZPLを送信する                                                                                                     |
| Get BT Stat     | Bluetooth 接続の状態をGetする                                                                                                    |
| open/send/close | 単枚印刷向けのシーケンス。<br />Bluetooth接続＞ZPLの送信 > Blueoothの切断を実施する                                              |
| Fast Print Mode | 連続・高速印刷向けのシーケンス。<br />Bluetooth接続＞ZPLの送信を実施。<br />※ Bluetooth Closeは実施しないので、別途実施が必要。 |


### 備考：プリンタ側の設定

プリンタ側で下記の設定をしておくこと。

```
! U1 setvar "bluetooth.discoverable" "on"
! U1 setvar "bluetooth.bluetooth.minimum_security_mode" "1"
! U1 setvar "bluetooth.enable" "on"
```
