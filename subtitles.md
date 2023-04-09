## slide 1

Gens & Lens
Making generators composable
by Aleksei Shamenev

sub:
Gens & Lens: ジェネレータを合成可能にする

## slide 2
Materials

sub:
発表資料です

## slide 3

Problem definition
Tech. stack
Composable gens

sub:
問題の提起、技術スタック
合成可能な gen

## slide 4

Westeros
Mobile bank service
Lots of corner cases in its validation logic
Using property-based tests for the logic
Problem definition
Bank accounts in Westeros

sub:
架空のウェスタロス大陸でモバイル・バンキングを作る
プロパティ・ベースでロジックをテストする

## slide 5

maginary world, imaginary regulations
Lots of regions in the world
Lots of different regulations for banks:
Personal bank accounts are forbidden in Westerlands. Both fiat and crypto.
 Crownlands allows using cryptocurrencies only for business and don't for personal use.
No cryptocurrencies are legal in Stormlands.
And only Free Cities have no regulations for banks.

sub:
地域によって異なる銀行規制。岩の王国では個人口座は禁止。
王室領では、仮想通貨は商用のみ。

## slide 6

Domain

sub:
ドメイン

## slide 7

Validation rules in Scala
Imaginary world, imaginary regulations

sub:
Scala での検証ルール

## slide 8

Scala 3
ScalaCheck
Monocle
Tech. stack

no subs

## slide 9

Property-based tests
Gen[T]
ScalaCheck

sub:
プロパティ・ベース・テスト

## slide 10

Lens
Optional

Monocle

sub:
不変データ構造のアクセスを補助するライブラリ

## slide 11


Manual composition
Lots of boilerplate code with .copy(..)

sub:
.copy(...) を何度も手で書くのが面倒

## slide 12

Using Monocle
Optics definition

sub:
Monocle を使うときれいに書ける

## slide 13

DSL

no subs

## slide 14

Test case

no subs

## slide 15

Implicit optics
Define optics using `given`

sub:
given を使ったオプティクスの定義

## slide 16

Implicit optics
DSL

no subs

## slide 17

Implcit optics
Test case

## slide 18

Optics autoderivation
No need to explicitly define optics
More requirements to the structures
Unique types for fields within the structure of cases classes
Or explicit markup of fields
Auto Derivation
Further work

sub:
今後の課題はオプティクスの自動導出

## slide 19

The End

sub:
ご清聴ありがとうございました
