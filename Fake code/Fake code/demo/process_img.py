import sys
from PIL import Image

def process_image(input_path, output_path):
    img = Image.open(input_path).convert("RGBA")
    data = img.getdata()
    
    # White background removal
    newData = []
    for item in data:
        # If it's close to white, make it transparent
        if item[0] > 230 and item[1] > 230 and item[2] > 230:
            newData.append((255, 255, 255, 0))
        else:
            newData.append(item)
            
    img.putdata(newData)
    
    # Auto-crop the transparent padding
    bbox = img.getbbox()
    if bbox:
        img = img.crop(bbox)
        
    img.save(output_path, "PNG")
    print(f"Image processed and saved to {output_path}")

if __name__ == '__main__':
    in_path = r"C:\Users\Mateo\.gemini\antigravity-ide\brain\013d91b0-6a1f-4482-af0f-d8c8d77c02d1\pixel_virus_boss_1780354611015.png"
    out_path = r"c:\Users\Mateo\Desktop\Trabajos de la U\proyecto\demo\src\main\resources\com\proyecto\boss.png"
    process_image(in_path, out_path)
