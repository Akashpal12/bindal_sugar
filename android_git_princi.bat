git add .
set /P Input=Enter Commit Message :
git commit -m "Updated at %DATE% %TIME% %Input%"
git push origin princi
@pause