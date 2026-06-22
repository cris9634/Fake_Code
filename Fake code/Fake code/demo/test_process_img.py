import unittest
import os
from PIL import Image
from process_img import process_image

class TestProcessImage(unittest.TestCase):

    def setUp(self):
        self.test_in_path = "test_in.png"
        self.test_out_path = "test_out.png"
        
        # Create a dummy image: 10x10 white background with a 2x2 red square in the middle
        img = Image.new("RGBA", (10, 10), (255, 255, 255, 255))
        pixels = img.load()
        for x in range(4, 6):
            for y in range(4, 6):
                pixels[x, y] = (255, 0, 0, 255)
                
        img.save(self.test_in_path)

    def tearDown(self):
        if os.path.exists(self.test_in_path):
            os.remove(self.test_in_path)
        if os.path.exists(self.test_out_path):
            os.remove(self.test_out_path)

    def test_process_image_removes_white_and_crops(self):
        # Run the processing function
        process_image(self.test_in_path, self.test_out_path)
        
        self.assertTrue(os.path.exists(self.test_out_path))
        
        # Check output image
        out_img = Image.open(self.test_out_path)
        
        # After removing white background and auto-cropping, 
        # it should only leave the 2x2 red square.
        self.assertEqual(out_img.size, (2, 2))
        
        # Check that the pixels are indeed red
        data = list(out_img.getdata())
        for pixel in data:
            self.assertEqual(pixel, (255, 0, 0, 255))

if __name__ == '__main__':
    unittest.main()
