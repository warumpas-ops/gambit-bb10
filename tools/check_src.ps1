Add-Type -AssemblyName System.Drawing

$src = 'C:\Users\Growth\.gemini\antigravity\brain\5921e6f2-9781-4865-8ce5-7ca3ea177b9b\gambit_icon_knight_olive_1795198638365.jpg'
$bmp = [System.Drawing.Bitmap]::FromFile($src)
Write-Host "Source Width: $($bmp.Width), Height: $($bmp.Height)"
Write-Host "Top-left (10,10): $($bmp.GetPixel(10,10))"
Write-Host "Top-left (50,50): $($bmp.GetPixel(50,50))"
Write-Host "Center ($($bmp.Width/2), $($bmp.Height/2)): $($bmp.GetPixel($bmp.Width/2, $bmp.Height/2))"
$bmp.Dispose()
