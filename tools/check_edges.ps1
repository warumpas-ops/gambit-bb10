Add-Type -AssemblyName System.Drawing

$src = 'C:\Users\Growth\.gemini\antigravity\brain\5921e6f2-9781-4865-8ce5-7ca3ea177b9b\gambit_icon_knight_olive_1795198638365.jpg'
$bmp = [System.Drawing.Bitmap]::FromFile($src)

Write-Host "Top edge (y=0):"
for ($x = 0; $x -lt 1024; $x += 100) {
    $c = $bmp.GetPixel($x, 0)
    Write-Host ("  x=" + $x + ": R=" + $c.R)
}

Write-Host "Left edge (x=0):"
for ($y = 0; $y -lt 1024; $y += 100) {
    $c = $bmp.GetPixel(0, $y)
    Write-Host ("  y=" + $y + ": R=" + $c.R)
}

Write-Host "Center row (y=512):"
Write-Host ("  x=0: " + $bmp.GetPixel(0, 512).R)
Write-Host ("  x=20: " + $bmp.GetPixel(20, 512).R)
Write-Host ("  x=50: " + $bmp.GetPixel(50, 512).R)
Write-Host ("  x=1023: " + $bmp.GetPixel(1023, 512).R)

Write-Host "Center col (x=512):"
Write-Host ("  y=0: " + $bmp.GetPixel(512, 0).R)
Write-Host ("  y=20: " + $bmp.GetPixel(512, 20).R)
Write-Host ("  y=50: " + $bmp.GetPixel(512, 50).R)
Write-Host ("  y=1023: " + $bmp.GetPixel(512, 1023).R)

$bmp.Dispose()
