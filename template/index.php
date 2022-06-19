<?php
error_reporting(0);

class render {
    private $tpl = '';
    private $tplattr = [];
    public function __construct($tpl, $arr = false)
    {
        if ($tpl == 'debug') {
            return phpinfo();
        }
        $this->tpl = __DIR__ . '/views/' .  str_replace("/.", "", $tpl);
        if ($arr && is_array($arr)) :
            $this->tplattr = $arr;
        endif;
        if ($this->check()):
            $this->run();
        else:
            echo "Error";
        endif;
    }

    private function check() {
        try{
            $arr = explode('.', $this->tpl);
            $ext = end($arr);
            if (in_array($ext, ['php', 'php2', 'php3', 'php4', 'php5', 'php6', 'php7', 'phtml'])):
                return false;
            endif;

            $content = file_get_contents($this->tpl);
            if (!$content):
                return false;
            endif;

            if ( preg_match('/script|<\?/i', $content) ):
                return false;
            endif;
        } catch (Exception $e) {
            return false;
        }

        return true;
    }

    private function run()
    {
        $content = file_get_contents($this->tpl);
        foreach ($this->tplattr as $attr => $value) {
            $content = str_replace("{{" . $attr ."}}", $value, $content);
        }
        echo $content;
    }
}

$tpl = 'tpl1.html';
$attr = array('name' => "player");
if (isset($_GET['tpl']) && is_string($_GET['tpl'])):
    $tpl = $_GET['tpl'];
endif;

if (isset($_GET['attr']) && is_array($_GET['attr'])):
    $attr = $_GET['attr'];
endif;

new render($tpl, $attr);