Add-Type -AssemblyName System.Drawing

$src = 'C:\Users\Growth\.gemini\antigravity\brain\5921e6f2-9781-4865-8ce5-7ca3ea177b9b\gambit_icon_knight_olive_1795198638365.jpg'
$original = [System.Drawing.Bitmap]::FromFile($src)
$w = $original.Width
$h = $original.Height

# Create 32-bit ARGB bitmap
$bmp = New-Object System.Drawing.Bitmap($w, $h, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
$g = [System.Drawing.Graphics]::FromImage($bmp)
$g.DrawImage($original, 0, 0, $w, $h)
$g.Dispose()
$original.Dispose()

# Flood fill from the 4 corners
$visited = New-Object 'bool[,]' $w, $h
$queue = New-Object System.Collections.Generic.Queue[System.Drawing.Point]

$queue.Enqueue((New-Object System.Drawing.Point(0, 0)))
$queue.Enqueue((New-Object System.Drawing.Point($w - 1, 0)))
$queue.Enqueue((New-Object System.Drawing.Point(0, $h - 1)))
$queue.Enqueue((New-Object System.Drawing.Point($w - 1, $h - 1)))

$count = 0
while ($queue.Count -gt 0) {
    $p = $queue.Dequeue()
    $x = $p.X
    $y = $p.Y
    if ($x -lt 0 -or $x -ge $w -or $y -lt 0 -or $y -ge $h) { continue }
    if ($visited[$x, $y]) { continue }
    $visited[$x, $y] = $true

    $c = $bmp.GetPixel($x, $y)
    # Check if pixel is white / light background
    # The olive border has R~133, G~160, B~60 (sum ~350). White background has R>220, G>220, B>220 (sum > 660).
    $brightness = ($c.R + $c.G + $c.B)
    if ($brightness -gt 500) {
        $bmp.SetPixel($x, $y, [System.Drawing.Color]::FromArgb(0, 0, 0, 0))
        $count++

        $queue.Enqueue((New-Object System.Drawing.Point($x + 1, $y)))
        $queue.Enqueue((New-Object System.Drawing.Point($x - 1, $y)))
        $queue.Enqueue((New-Object System.Drawing.Point($x, $y + 1)))
        $queue.Enqueue((New-Object System.Drawing.Point($x, $y - 1)))
    }
}

Write-Host "Total transparent corner pixels created: $count"

$outPath = 'C:\Users\Growth\.gemini\antigravity\scratch\gambit-bb10\tools\icon_transparent.png'
$bmp.Save($outPath, [System.Drawing.Imaging.ImageFormat]::Png)
Write-Host "Saved transparent icon to: $outPath"

# Verify corners are transparent
Write-Host "Corner (0,0): $($bmp.GetPixel(0,0))"
Write-Host "Corner ($($w-1),0): $($bmp.GetPixel($w-1,0))"
Write-Host "Corner (0,$($h-1)): $($bmp.GetPixel(0,$h-1))"
Write-Host "Corner ($($w-1),$($h-1)): $($bmp.GetPixel($w-1,$h-1))"
Write-Host "Center (512,512): $($bmp.GetPixel(512,512))"

$bmp.Dispose()
