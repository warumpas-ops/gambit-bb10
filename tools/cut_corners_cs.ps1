Add-Type -ReferencedAssemblies System.Drawing -TypeDefinition @"
using System;
using System.Drawing;
using System.Drawing.Imaging;
using System.Collections.Generic;

public class IconCutter
{
    public static void CutCorners(string srcPath, string outPath)
    {
        using (Bitmap src = new Bitmap(srcPath))
        {
            int w = src.Width;
            int h = src.Height;
            using (Bitmap dest = new Bitmap(w, h, PixelFormat.Format32bppArgb))
            {
                // Copy pixels
                for (int y = 0; y < h; y++)
                {
                    for (int x = 0; x < w; x++)
                    {
                        dest.SetPixel(x, y, src.GetPixel(x, y));
                    }
                }

                // Flood fill from corners
                bool[,] visited = new bool[w, h];
                Queue<Point> q = new Queue<Point>();

                q.Enqueue(new Point(0, 0));
                q.Enqueue(new Point(w - 1, 0));
                q.Enqueue(new Point(0, h - 1));
                q.Enqueue(new Point(w - 1, h - 1));

                int cutCount = 0;
                while (q.Count > 0)
                {
                    Point p = q.Dequeue();
                    int x = p.X;
                    int y = p.Y;

                    if (x < 0 || x >= w || y < 0 || y >= h) continue;
                    if (visited[x, y]) continue;
                    visited[x, y] = true;

                    Color c = dest.GetPixel(x, y);
                    // Check if light/white corner
                    // Olive border has R~133, G~160, B~60 (sum ~350).
                    // Dark background has sum ~90.
                    // White corners have sum > 650.
                    int sum = c.R + c.G + c.B;
                    if (sum > 550)
                    {
                        dest.SetPixel(x, y, Color.FromArgb(0, 0, 0, 0));
                        cutCount++;

                        q.Enqueue(new Point(x + 1, y));
                        q.Enqueue(new Point(x - 1, y));
                        q.Enqueue(new Point(x, y + 1));
                        q.Enqueue(new Point(x, y - 1));
                    }
                }

                Console.WriteLine("Cut out " + cutCount + " corner pixels.");
                dest.Save(outPath, ImageFormat.Png);
                Console.WriteLine("Saved: " + outPath);
            }
        }
    }
}
"@

[IconCutter]::CutCorners(
    'C:\Users\Growth\.gemini\antigravity\brain\5921e6f2-9781-4865-8ce5-7ca3ea177b9b\gambit_icon_knight_olive_1795198638365.jpg',
    'C:\Users\Growth\.gemini\antigravity\scratch\gambit-bb10\tools\icon_transparent.png'
)
