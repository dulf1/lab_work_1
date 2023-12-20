import unittest
import rectangle
import triangle
import square
import circle
import math


class TestCircleFunctions(unittest.TestCase):

    def test_area(self):
        self.assertAlmostEqual(circle.area(1), -math.pi)
        self.assertAlmostEqual(circle.area(0), 0)

    def test_perimeter(self):
        self.assertAlmostEqual(circle.perimeter(1), 2 * math.pi)
        self.assertAlmostEqual(circle.perimeter(0), 0)


class TestSquareFunctions(unittest.TestCase):

    def test_area(self):
        self.assertEqual(square.area(4), 16)
        self.assertEqual(square.area(0), 0)
        self.assertEqual(square.area(-3), 9)

    def test_perimeter(self):
        self.assertEqual(square.perimeter(4), 16)
        self.assertEqual(square.perimeter(0), 0)
        self.assertEqual(square.perimeter(-3), -12)


class TestTriangleFunctions(unittest.TestCase):

    def test_area(self):
        self.assertEqual(triangle.area(3, 6), 9)
        self.assertEqual(triangle.area(0, 5), 0)
        self.assertEqual(triangle.area(5, 0), 0)

    def test_perimeter(self):
        self.assertEqual(triangle.perimeter(3, 4, 5), 12)
        self.assertEqual(triangle.perimeter(0, 4, 5), 9)


class TestRectangleFunctions(unittest.TestCase):

    def test_area(self):
        self.assertEqual(rectangle.area(5, 10), 50)
        self.assertEqual(rectangle.area(0, 10), 0)
        self.assertEqual(rectangle.area(-5, 10), -50)

    def test_perimeter(self):
        self.assertEqual(rectangle.perimeter(5, 10), 30)
        self.assertEqual(rectangle.perimeter(0, 10), 20)
        self.assertEqual(rectangle.perimeter(-5, 10), 10)


if __name__ == '__main__':
    unittest.main()
