Add-Type -AssemblyName System.Drawing

$files = @(
    'C:\Users\Growth\.gemini\antigravity\scratch\gambit-bb10\app\src\main\res\mipmap-mdpi\ic_launcher.png',
    'C:\Users\Growth\.gemini\antigravity\scratch\gambit-bb10\app\src\main\res\mipmap-hdpi\ic_launcher.png',
    'C:\Users\Growth\.gemini\antigravity\scratch\gambit-bb10\app\src\main\res\mipmap-xhdpi\ic_launcher.png',
    'C:\Users\Growth\.gemini\antigravity\scratch\gambit-bb10\app\src\main\res\mipmap-xxhdpi\ic_launcher.png'
)

foreach ($f in $files) {
    $bmp = [System.Drawing.Bitmap]::FromFile($f)
    $w = $bmp.Width
    $h = $bmp.Height
    Write-Host "$($w)x$($h):"
    Write-Host "   TopLeft (0,0): $($bmp.GetPixel(0,0))"
    Write-Host "   TopRight ($($w-1),0): $($bmp.GetPixel($w-1,0))"
    Write-Host "   BottomLeft (0,$($h-1)): $($bmp.GetPixel(0,$h-1))"
    Write-Host "   BottomRight ($($w-1),$($h-1)): $($bmp.GetPixel($w-1,$h-1))"
    $bmp.Dispose()
}
