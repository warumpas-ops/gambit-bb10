Add-Type -AssemblyName System.Drawing

$path = 'C:\Users\Growth\.gemini\antigravity\scratch\gambit-bb10\app\src\main\res\mipmap-xxhdpi\ic_launcher.png'
$bmp = [System.Drawing.Bitmap]::FromFile($path)
Write-Host "Width: $($bmp.Width), Height: $($bmp.Height)"
Write-Host "Pixel (0,0): $($bmp.GetPixel(0,0))"
Write-Host "Pixel (143,0): $($bmp.GetPixel($bmp.Width-1,0))"
Write-Host "Pixel (0,143): $($bmp.GetPixel(0,$bmp.Height-1))"
Write-Host "Pixel (143,143): $($bmp.GetPixel($bmp.Width-1,$bmp.Height-1))"
Write-Host "Pixel (20,20): $($bmp.GetPixel(20,20))"
Write-Host "Pixel (72,72): $($bmp.GetPixel(72,72))"
$bmp.Dispose()
