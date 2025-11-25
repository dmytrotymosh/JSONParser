# Перше завданння

Програма-парсер містить сутність за замовчуванням, яка слугує шаблоном для генерації JSON-файлів:
```java
public class CNCMachine {
    public String maker;
    public String countryOfOrigin;
    public String model;
    public double length;
    public double width;
    public double height;
    public double weight;
    public double tableWorkArea;
    public int maxPowerConsumption;
    public int workingVoltage;
    public String commandLanguage;
    public String operatingSystem;
}
```
CNCMachine являє собою модель станка ЧПУ (числовим програмним управлінням). 
Приклад файлу, парсинг якого програма здійснює за замовчуванням:
```json
[ {
  "maker" : "Haas Automation",
  "countryOfOrigin" : "South Korea",
  "model" : "VF-2 # Integrex i-200",
  "length" : 3700.0,
  "width" : 3900.0,
  "height" : 3600.0,
  "weight" : 1740.0,
  "tableWorkArea" : 940.0,
  "maxPowerConsumption" : 13000,
  "workingVoltage" : 400,
  "commandLanguage" : "Mazatrol",
  "operatingSystem" : "Fanuc 31i"
}]
```
Приклад вихідних даних:
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<statistics>
  <item>
    <value>380</value>
    <count>2501097</count>
  </item>
  <item>
    <value>400</value>
    <count>2498903</count>
  </item>
</statistics>
```

Окрім роботи безпосередньо з моделлю CNCMachine, програма здатна формувати статистику для будь-яких об'єктів у файлах JSON,
якщо буде передано шлях до директорії та назву ключа, який необхідно обробляти (якщо значення ключа не містить вкладений об'єкт
або масив об'єктів/масивів).
При першому запуску програми у режимі за замовчуванням (без надання шляху до сторонньої директорії) відбувається 
генерація п'ятисот файлів місткістю 10 000 об'єктів у кожному, на основі яких і рахуватиметься статистика. Виконання цього
кроку необхідне для формування даних для здійснення бенчмарку.

## Результати експериментів з багатопотоковим виконанням
Тестування впливу кількості потоків на швидкість парсингу файлів JSON відбувалося з використанням інструмента JMH.
Тестовий набір даних містив 200 файлів по 100 000 об'єктів у кожному, його розмір перевищував 7 GB. Після запуску бенчмарку
вдалося отримати наступні результати:
```text
Benchmark                           (threads)  Mode  Cnt      Score      Error  Units
JsonParserBenchmark.benchmarkParse          1  avgt    5  62186,513 ? 2752,559  ms/op
JsonParserBenchmark.benchmarkParse          2  avgt    5  33814,295 ? 1761,267  ms/op
JsonParserBenchmark.benchmarkParse          4  avgt    5  25130,771 ? 2001,507  ms/op
JsonParserBenchmark.benchmarkParse          8  avgt    5  26126,128 ? 1032,488  ms/op
```
Можемо зробити висновок, що використання чотирьох потоків продемонструвало найбільшу швидкість виконання. 2 потоки на 84%
(майже у 2 рази) швидше впоралися з парсингом двадцяти мільйонів об'єктів, 4 потоки підвищили швидкодію на 34% у порівняння 
з двома (та 147% у порівнянні з однопотоковим виконанням), тоді як 8 потоків засвідчили надмірність такої кількості, адже
нівелюють паралельність виконання накладними витратами на перемикання контексту.

## Запуск бенчмарку
Перед запуском бенчмарку необхідно принаймні один раз запустити програму, після чого необхідно виконати наступні команди 
з кореня проекту:
```text
mvn clean
mvn package
java -jar target/jmh-benchmarks.jar
```