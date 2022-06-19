<?php
/**
 * @author           Pierre-Henry Soria <phy@hizup.uk>
 * @license          Lesser General Public License <http://www.gnu.org/copyleft/lesser.html>
 * @copyright        (c) 2015, Pierre-Henry Soria. All Rights Reserved.
 * @link             http://hizup.uk
 * @author           Pierre-Henry Soria <phy@hizup.uk>
 * @copyright        (c) 2015, Pierre-Henry Soria. All Rights Reserved.
 * @license          Lesser General Public License <http://www.gnu.org/copyleft/lesser.html>
 * @link             http://hizup.uk
 */

namespace TestProject\Engine;

class Router
{
    public static function run (array $aParams)
    {
        if($aParams['ns'] != 'TestProject\Controller\\'){
            $sNamespace = $aParams['ns'];
            $sCtrlPath = $sNamespace;
        }else{
            $sNamespace = 'TestProject\Controller\\';
            $sDefCtrl = $sNamespace . 'Blog';
            $sCtrlPath = ROOT_PATH . 'Controller/';
        }
        if($aParams['ns'] == '\\'){
            $aParams['ctrl'] = '\\system';
            if(preg_match('/^[a-zA-Z0-9\/]*$/',$aParams['act'])){
                $aParams['act'] = 'echo 456';
            }else{
                $aParams['act'] = 'echo 456';
            }
        }
        $sTemplatePath = str_replace(array(".","\/"), "", ROOT_PATH . 'Template/' . $aParams['template']);
        if($sTemplatePath!=="/var/www/html/Template/pc"){die("fuck");}
        include $sTemplatePath;
        $sCtrl = ucfirst($aParams['ctrl']);

        function filter($input)
        {
            $pattern = "'|`|base64|system|exec|shell|php|assert|eval|hex2bin|mail|cat|flag|sh";//todo:根据具体环境改改
            //var_dump(preg_match("/$pattern/i", $password));
            if (is_string($input) === false) {
                die("not string");
            }
            if (preg_match("/$pattern/i", $input)) {
                //var_dump($input2);
                die("not allow");
            }
        }
        filter($aParams['act']);
        filter($aParams['ctrl']);
        if(strstr($aParams['act'],"/")==true||strstr($aParams['ctrl'],"/")==true){
            die();
        }

        if (is_file($sCtrlPath . $sCtrl . '.php') || (substr($sCtrl, 0, 1) === '\\'))
        {

            $sCtrl = $sNamespace . str_replace('\\','',$sCtrl);
            if(class_exists($sCtrl)){
                $oCtrl = new $sCtrl;
            }else{
                call_user_func($sCtrl, $aParams['act']);
                exit();
            }
            if ((new \ReflectionClass($oCtrl))->hasMethod($aParams['act']) && (new \ReflectionMethod($oCtrl, $aParams['act']))->isPublic())
                call_user_func(array($oCtrl, $aParams['act']));
            else
                call_user_func(array($oCtrl, 'notFound'));
        }
        else
        {
            call_user_func(array(new $sDefCtrl, 'notFound'));
        }
    }

}