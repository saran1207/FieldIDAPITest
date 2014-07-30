<h3>Enabled Modules</h3>
<div id="toggleInspectionsContainer" class="modules">
    <@s.form id="toggleInspectionsForm" action="toggleInspectionsEnabled" namespace="/adminAjax" theme="fieldidSimple">
        <@s.hidden name="id"/>
        <span class="fieldHolder">
            <@s.checkbox name="tenant.settings.inspectionsEnabled" onclick="toggleInspectionsEnabled()"/>
        </span>
        <label>Inspections</label>
    </@s.form>
</div>
<div id="toggleLotoContainer" class="modules">
    <@s.form id="toggleLotoForm" action="toggleLotoEnabled" namespace="/adminAjax" theme="fieldidSimple">
        <@s.hidden name="id"/>
        <span class="fieldHolder">
            <@s.checkbox name="tenant.settings.lotoEnabled" onclick="toggleLotoEnabled()"/>
        </span>
        <label>LOTO</label>
    </@s.form>
</div>