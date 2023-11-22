## Лабораторная работа
## Содержание:
- [Общее описание решения](#Общее описания решения)
- [Описание функций для файла circle.py](#Описание функций для файла circle.py:)
- [Описание функций для файла rectangle.py](#Описание функций для файла rectangle.py:)
- [Описание функций для файла square.py:](#Описание функций для файла square.py:)
- [Описание функций для файла triangle.py](#Описание функций для файла triangle.py:)
- [История изменения проектов с хэшами коммитов](#История изменения проектов с хэшами коммитов)

### Общее описания решения
1. Выполнил команду **git clone**.
- *команда **git clone** копирует репозиторий по ссылке*
2. Создал новую ветку new_features_<мой код ИСУ>.
- *команда **git branch** создаёт новую ветку*
- *команда **git checkout** переводит на ветку*
3. Добавил новый файл в эту ветку.
4. Изменил содержимое файла.
5. Выполнил команду **git add** для этого файла.
- *команда **git add** добавляет файл в репозиторий*
6. Сделал коммит с сообщением о том, что был добавлен новый файл.
- *команда **git commit – m "комментарий"** сохраняет сделанные ранее изменения и добавляет комментарий*
7. Добавил ещё один один файл в эту ветку.
8. Выполнил **git add** для нового файла.
- *команда **git add** добавляет файл в репозиторий*
9. Исправил ошибку в вычислении периметра в файле rectangle.py.
10. Создал ещё один коммит внутри этой же ветки с сообщением о том, что была исправлена ошибка.
- *команда **git commit -m "комментарий"** сохраняет сделанные ранее изменения и добавляет комментарий*
11. Построил граф истории всего репозитория с однострочным выводом коммитов.
- *команда **git log –graph –oneline –all** выводит все комментарии*
12. Построил граф истории только текущей ветки.
- *команда **git log –graph –oneline** выводит комментарии только текущей ветки*
13. Взял хэши двух последних коммитов из истории и посмотрел, какие изменения были внесены.
- *команда **git show** показывает сохранённые конкретным коммитом изменения*
14. Выполнил merge в ветку main.
- *команда **git checkout main** переключает на ветку main*
15. Сделал **Pull Reqest** и обсудил с рецензентом в процессе Code Review.
- *команда **git remote set-url origin** задает url указанного по ссылке репозитория*
- *команда **git push -u origin** связывает текущую ветку с указанной веткой удаленного
репозитория*
- *команда **git remote -v** показывает наполнение удаленного репозитория*

16. Удалил созданную ветку.
- *команда **git branch -d** удаляет заданную ветку*
17. Добавил описание решения для фалов **circle.py**, **rectangle.py**, **square.py**, **triangle.py**.
18. Выполнил **git add** для файлов **circle.py**, **rectangle.py**, **square.py**, **triangle.py**.
- *команда **git add** добавляет файл в репозиторий*
19. Выполнил **git commit**.
- *команда **git commit – m "комментарий"** сохраняет сделанные ранее изменения и добавляет комментарий.*
20. Выполнил **git add** файла **README.md**.
- *команда **git add** добавляет файл в репозиторий*

### <font color="Blac"> Описание функций для файла circle.py:
***
><font color="white">def area(r):  
&nbsp;&nbsp;&nbsp;&nbsp;return math.pi * r * r
***
- #### *<font color="blac">Принимает число r - радиус.* *Возвращает площадь круга с радиусом r* 

>  <font color="white">*Примеры вызова:*  
  input: 10  
  output: ~157,07964
***
><font color="white">def perimeter(r):  
&nbsp;&nbsp;&nbsp;&nbsp;return 2 * math.pi * r
***
- #### <font color="blac">Принимает число r - радиус. Возвращает длину окружности с радиусом r  
><font color="white">*Примеры вызова:*  
  input: 6  
  output: ~37,69912

### <font color="blac"> Описание функций для файла rectangle.py:
***
><font color="white">def area(a, b):  
&nbsp;&nbsp;&nbsp;&nbsp;return a * b
***
- #### <font color="blac">Принимает 2 числа a и b. Возвращает площадь прямоугольника со сторонами длины a и b   
><font color="white">*Примеры вызова:*  
  input: 8 3  
  output: 24

***
><font color="white">def perimeter(a, b):  
&nbsp;&nbsp;&nbsp;&nbsp;return 2 * a + 2 * b
***
- #### <font color="blac">Принимает 2 числа a и b. Возвращает периметр прямоугольника со сторонами длины a и b  
><font color="white">*Примеры вызова:*  
  input: 8 16  
  output: 48

### <font color="blac"> Описание функций для файла square.py:
***
><font color="white">def area(a):  
&nbsp;&nbsp;&nbsp;&nbsp;return a * a
***
- #### <font color="blac">Принимает число a. Возвращает площадь квадрата со стороной длины a   
><font color="white">*Примеры вызова:*  
  input: 10  
  output: 100
***
><font color="white">def perimeter(a, b):  
&nbsp;&nbsp;&nbsp;&nbsp;return 4 * a
***
- #### <font color="blac">Принимает число a. Возвращает периметр квадрата со стороной длины a  
><font color="white">*Примеры вызова:*  
  input: 4  
  output: 16

### <font color="blac"> Описание функций для файла triangle.py:
***
><font color="white">def area(a, h):  
&nbsp;&nbsp;&nbsp;&nbsp;return a * h / 2
***
- #### <font color="blac">Принимает 2 числа a и h. Возвращает площадь треугольника со стороной длины a и высотой длины h, проведённой к стороне длины a   
><font color="white">*Примеры вызова:*  
  input: 5 8  
  output: 20
***
><font color="white">def perimeter(a, b, c):  
&nbsp;&nbsp;&nbsp;&nbsp;return a + b + c
***
- #### <font color="blac">Принимает 3 числа a, b, c. Возвращает периметр треугольника со сторонами длины a, b, c  
><font color="white">*Примеры вызова:*  
  input: 5 3 4  
  output: 12

### <font color="blac"> История изменения проектов с хэшами коммитов
<font color="white">commit 943d99522e2cd361b2a1262ba4295bb3424073b5     
Author: dulfi <olegg.kudrin@mail.com>   
Date:   Mon Sep 25 15:25:12 2023 +0300  
*'''rectangle.py was fixed'''*


<font color="white">commit 938dec91dd1c7f7cd0a3bf4aceb58a61ca3b4ba8
Author: dulfi <olegg.kudrin@mail.com>   
Date:   Mon Sep 25 15:23:48 2023 +0300      
*'''rectangle.py was added'''*

<font color="white">commit d078c8d9ee6155f3cb0e577d28d337b791de28e2  
Author: smartiqa <info@smartiqa.ru>  
Date:   Thu Mar 4 14:55:29 2021 +0300  
*'''L-03: Docs added'''*

<font color="white">commit 8ba9aeb3cea847b63a91ac378a2a6db758682460  
Author: smartiqa <info@smartiqa.ru>  
Date:   Thu Mar 4 14:54:08 2021 +0300  
*'''L-03: Circle and square added'''*

Unit tests для функции unitTests.
Описание файла
В файле unitTests.py представлен набор юнит-тестов для проверки математической функции синус (sin) из стандартной библиотеки math Python. Тесты предназначены для проверки корректности работы функции sin для ключевых значений в пределах одного периода функции.

Включенные тесты
В файле unitTests.py реализованы следующие тесты:

test_area: Проверяет, что area(1) равен 1*pi, а также area(0) равен 0.
test_perimeter: Проверяет, что perimeter(1) равен 2*pi, а также perimeter(0) равен 0.

Запуск тестов
Для запуска тестов перейдите в директорию проекта и выполните следующую команду:

python -m unittest unitTests.py