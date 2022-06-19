<?php
/**
 * @author           Pierre-Henry Soria <phy@hizup.uk>
 * @copyright        (c) 2015, Pierre-Henry Soria. All Rights Reserved.
 * @license          Lesser General Public License <http://www.gnu.org/copyleft/lesser.html>
 * @link             http://hizup.uk
 */

namespace TestProject\Engine\Pattern;

trait Node{
    public function __autoload(){
        $admin = new \TestProject\Controller\Admin;
        $admin->adminupload();
    }
}