@echo off
echo ========================================
echo BeMan 内容审核系统测试
echo ========================================
echo.

echo 正在运行单元测试...
mvn test -Dtest=ContentAuditServiceTest

echo.
echo 正在运行控制器测试...
mvn test -Dtest=ContentAuditControllerTest

echo.
echo 正在运行模型测试...
mvn test -Dtest=AuditResultTest

echo.
echo 正在运行集成测试...
mvn test -Dtest=ContentAuditIntegrationTest

echo.
echo ========================================
echo 所有测试完成！
echo ========================================
pause
