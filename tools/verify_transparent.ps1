Add-Type -AssemblyName System.Drawing
$bmp = [System.Drawing.Bitmap]::FromFile('C:\Users\Growth\.gemini\antigravity\scratch\gambit-bb10\tools\icon_transparent.png')
$w = $bmp.Width
$h = $bmp.Height
Write-Host "Corner (0,0): $($bmp.GetPixel(0,0))"
Write-Host "Corner ($($w-1),0): $($bmp.GetPixel($w-1,0))"
Write-Host "Corner (0,$($h-1)): $($bmp.GetPixel(0,$h-1))"
Write-Host "Corner ($($w-1),$($h-1)): $($bmp.GetPixel($w-1,$h-1))"
Write-Host "Edge (512,0): $($bmp.GetPixel(512,0))"
Write-Host "Center (512,512): $($bmp.GetPixel(512,512))"
$bmp.Dispose()
