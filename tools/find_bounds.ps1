Add-Type -AssemblyName System.Drawing

$src = 'C:\Users\Growth\.gemini\antigravity\brain\5921e6f2-9781-4865-8ce5-7ca3ea177b9b\gambit_icon_knight_olive_1795198638365.jpg'
$bmp = [System.Drawing.Bitmap]::FromFile($src)
$w = $bmp.Width
$h = $bmp.Height

# Check horizontal center line across X
$minX = $w; $maxX = 0
$minY = $h; $maxY = 0

for ($y = 0; $y -lt $h; $y += 4) {
    for ($x = 0; $x -lt $w; $x += 4) {
        $c = $bmp.GetPixel($x, $y)
        # If not white (R < 240 or G < 240 or B < 240)
        if ($c.R -lt 240 -or $c.G -lt 240 -or $c.B -lt 240) {
            if ($x -lt $minX) { $minX = $x }
            if ($x -gt $maxX) { $maxX = $x }
            if ($y -lt $minY) { $minY = $y }
            if ($y -gt $maxY) { $maxY = $y }
        }
    }
}

Write-Host "Icon Bounding Box:"
Write-Host "minX: $minX, maxX: $maxX (Width: $($maxX - $minX))"
Write-Host "minY: $minY, maxY: $maxY (Height: $($maxY - $minY))"
$bmp.Dispose()
