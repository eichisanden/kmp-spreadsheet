# KMP Spreadsheet

Kotlin Multiplatformで実装されたGoogle Spreadsheetのようなコンポーネントです。

## 機能

### 1. セル入力
- 各セルをクリックして値を編集できます
- 選択されたセルはハイライト表示されます

### 2. 行の高さ調整
- 各行の下部をドラッグして高さを変更できます
- 高さは24dpから200dpの範囲で調整可能です

### 3. ツリー構造
- 行を階層構造で表示できます
- 親行の展開/折りたたみボタンで子行の表示を切り替えられます
- インデントで階層レベルを視覚的に表現

### 4. 列の幅調整
- ヘッダーの右端をドラッグして列の幅を変更できます
- 幅は60dpから400dpの範囲で調整可能です

## プロジェクト構成

```
kmp-spreadsheet/
├── shared/                 # 共通コード (Kotlin Multiplatform)
│   └── src/
│       └── commonMain/
│           └── kotlin/
│               └── com/example/spreadsheet/
│                   ├── data/model/          # データモデル
│                   ├── ui/components/       # UIコンポーネント
│                   └── ui/viewmodel/        # ViewModel
└── androidApp/            # Androidアプリ
    └── src/main/
        └── kotlin/com/example/spreadsheet/android/
```

## ビルド方法

### 前提条件
- JDK 17以上
- Android Studio Hedgehog (2023.1.1) 以上
- Kotlin 1.9.20

### Android アプリをビルド

```bash
./gradlew :androidApp:assembleDebug
```

### Android アプリを実行

```bash
./gradlew :androidApp:installDebug
```

または Android Studio で `androidApp` をビルドして実行してください。

## 使い方

### セルの編集
1. セルをクリックして選択
2. テキストを入力
3. 別のセルをクリックして確定

### 行の高さ変更
- 行の下部境界線をドラッグ

### ツリーの展開/折りたたみ
- 親行の左側にある矢印アイコンをクリック
- 下向き矢印: 展開状態
- 右向き矢印: 折りたたみ状態

### 列の幅変更
- ヘッダーの右端境界線をドラッグ

## 技術スタック

- **Kotlin Multiplatform**: クロスプラットフォーム対応
- **Jetpack Compose**: UIフレームワーク
- **StateFlow**: 状態管理
- **Material3**: デザインシステム

## アーキテクチャ

- **MVVM パターン**: ViewModel による状態管理
- **単方向データフロー**: StateFlow を使用
- **Composable UI**: 宣言的UIで実装

## ライセンス

MIT License
