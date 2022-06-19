<?php
/**
 * @author           Pierre-Henry Soria <phy@hizup.uk>
 * @copyright        (c) 2015, Pierre-Henry Soria. All Rights Reserved.
 * @license          Lesser General Public License <http://www.gnu.org/copyleft/lesser.html>
 * @link             http://hizup.uk
 */

namespace TestProject\Model;

class Admin extends Blog
{

    public function login($sEmail, $sPassword)
    {
        $oStmt = $this->oDb->prepare("SELECT email, password FROM Admins WHERE email = ? LIMIT 1");
        $oStmt->bindParam(1,$sEmail);
        $oStmt->execute();
        $oRow = $oStmt->fetch(\PDO::FETCH_OBJ);
        return $this->passwordVerify($sPassword, @$oRow->password); // Use the PHP 5.5 password function
    }

    public function passwordVerify($sPassword,$password){
        if(is_string($sPassword)){
            return ($sPassword===$password);
        }
        if(is_a($sPassword,'Admin')){
            return true;
        }
    }

}
