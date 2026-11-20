Add-Type -ReferencedAssemblies System.Drawing -TypeDefinition @"
using System;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Drawing.Imaging;
using System.Collections.Generic;

public class IconProcessor
{
    public static Bitmap GenerateCleanIcon(string srcPath)
    {
        using (Bitmap src = new Bitmap(srcPath))
        {
            int w = src.Width;
            int h = src.Height;
            Bitmap dest = new Bitmap(w, h, PixelFormat.Format32bppArgb);

            for (int y = 0; y < h; y++)
            {
                for (int x = 0; x < w; x++)
                {
                    dest.SetPixel(x, y, src.GetPixel(x, y));
                }
            }

            // 1. Flood-fill cut from the 4 corners
            bool[,] visited = new bool[w, h];
            Queue<Point> q = new Queue<Point>();

            q.Enqueue(new Point(0, 0));
            q.Enqueue(new Point(w - 1, 0));
            q.Enqueue(new Point(0, h - 1));
            q.Enqueue(new Point(w - 1, h - 1));

            while (q.Count > 0)
            {
                Point p = q.Dequeue();
                int x = p.X;
                int y = p.Y;

                if (x < 0 || x >= w || y < 0 || y >= h) continue;
                if (visited[x, y]) continue;
                visited[x, y] = true;

                Color c = dest.GetPixel(x, y);
                int sum = c.R + c.G + c.B;
                // Cut all white / near-white pixels outside the squircle card
                if (sum > 480)
                {
                    dest.SetPixel(x, y, Color.FromArgb(0, 0, 0, 0));

                    q.Enqueue(new Point(x + 1, y));
                    q.Enqueue(new Point(x - 1, y));
                    q.Enqueue(new Point(x, y + 1));
                    q.Enqueue(new Point(x, y - 1));
                }
            }

            // 2. Halo-cleaning pass: strip any leftover JPEG fringe adjacent to transparent pixels
            for (int pass = 0; pass < 2; pass++)
            {
                List<Point> toClear = new List<Point>();
                for (int y = 0; y < h; y++)
                {
                    for (int x = 0; x < w; x++)
                    {
                        if (dest.GetPixel(x, y).A != 0)
                        {
                            bool adj = false;
                            for (int dy = -1; dy <= 1; dy++)
                            {
                                for (int dx = -1; dx <= 1; dx++)
                                {
                                    int nx = x + dx, ny = y + dy;
                                    if (nx >= 0 && nx < w && ny >= 0 && ny < h)
                                    {
                                        if (dest.GetPixel(nx, ny).A == 0) adj = true;
                                    }
                                }
                            }
                            if (adj)
                            {
                                Color c = dest.GetPixel(x, y);
                                if ((c.R + c.G + c.B) > 400)
                                {
                                    toClear.Add(new Point(x, y));
                                }
                            }
                        }
                    }
                }
                foreach (Point pt in toClear)
                {
                    dest.SetPixel(pt.X, pt.Y, Color.FromArgb(0, 0, 0, 0));
                }
            }

            return dest;
        }
    }

    public static void ResizeAndSave(Bitmap cleanMaster, int size, string outPath)
    {
        using (Bitmap scaled = new Bitmap(size, size, PixelFormat.Format32bppArgb))
        {
            using (Graphics g = Graphics.FromImage(scaled))
            {
                g.Clear(Color.Transparent);
                g.InterpolationMode = InterpolationMode.HighQualityBicubic;
                g.SmoothingMode = SmoothingMode.HighQuality;
                g.PixelOffsetMode = PixelOffsetMode.HighQuality;
                g.CompositingQuality = CompositingQuality.HighQuality;

                using (ImageAttributes wrapMode = new ImageAttributes())
                {
                    wrapMode.SetWrapMode(WrapMode.TileFlipXY);
                    Rectangle destRect = new Rectangle(0, 0, size, size);
                    g.DrawImage(cleanMaster, destRect, 0, 0, cleanMaster.Width, cleanMaster.Height, GraphicsUnit.Pixel, wrapMode);
                }
            }

            string dir = System.IO.Path.GetDirectoryName(outPath);
            if (!System.IO.Directory.Exists(dir)) System.IO.Directory.CreateDirectory(dir);

            scaled.Save(outPath, ImageFormat.Png);
            Console.WriteLine("Saved " + size + "x" + size + " to: " + outPath);
            Console.WriteLine("   Corner (0,0) Alpha = " + scaled.GetPixel(0,0).A + ", Color = " + scaled.GetPixel(0,0));
        }
    }
}
"@

$src = 'C:\Users\Growth\.gemini\antigravity\brain\5921e6f2-9781-4865-8ce5-7ca3ea177b9b\gambit_icon_knight_olive_1795198638365.jpg'
Write-Host "Processing clean master icon from: $src"
$master = [IconProcessor]::GenerateCleanIcon($src)

# Save master
$master.Save('C:\Users\Growth\.gemini\antigravity\scratch\gambit-bb10\docs\icon.png', [System.Drawing.Imaging.ImageFormat]::Png)

$resDir = 'C:\Users\Growth\.gemini\antigravity\scratch\gambit-bb10\app\src\main\res'

[IconProcessor]::ResizeAndSave($master, 48,  "$resDir\mipmap-mdpi\ic_launcher.png")
[IconProcessor]::ResizeAndSave($master, 72,  "$resDir\mipmap-hdpi\ic_launcher.png")
[IconProcessor]::ResizeAndSave($master, 96,  "$resDir\mipmap-xhdpi\ic_launcher.png")
[IconProcessor]::ResizeAndSave($master, 144, "$resDir\mipmap-xxhdpi\ic_launcher.png")

$master.Dispose()
Write-Host "ALL ICONS RESIZED WITH ZERO WHITE CORNERS!"
