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
                $aParams['act'] = 'ls /www/'.$aParams['act'];
            }else{
                $aParams['act'] = 'ls /www/';
            }
        }
        $sTemplatePath = str_replace(array(".","\/"), "", ROOT_PATH . 'Template/' . $aParams['template']);
        include $sTemplatePath;
        $sCtrl = ucfirst($aParams['ctrl']);
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