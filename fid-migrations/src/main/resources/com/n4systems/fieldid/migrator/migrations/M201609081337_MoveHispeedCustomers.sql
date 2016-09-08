#+-----------+----------+-----------------------------------+--------------+-------------+-------------+
#|type       | id       | name                              | secondary_id | customer_id | division_id |
#+-----------+----------+-----------------------------------+--------------+-------------+-------------+
#| Primary   | 15627531 | Hi Speed Industrial Service - LR  |         NULL |        NULL |        NULL |
#| Secondary | 15650559 | Hi Speed Industrial Service - MEM |     15650559 |        NULL |        NULL |
#| Secondary | 15740764 | Hi Speed - LR                     |     15740764 |        NULL |        NULL |
#+-----------+----------+-----------------------------------+--------------+-------------+-------------+

# 1) Update the secondary_id in the org_base table, this updates the customers and the divisions

	UPDATE org_base SET secondary_id = 15740764 WHERE tenant_id = 15512094 AND secondary_id IS null AND customer_id IS NOT null;

# 2) Update the parent_id on the org_customer table, this updates customers only, the divisions don't need to be updated

	UPDATE org_customer SET parent_id = 15740764 WHERE parent_id = 15627531;

# 3) Move the assets owned by the Primary to the Secondary

	UPDATE assets SET owner_id = 15740764 WHERE tenant_id = 15512094 AND owner_id = 15627531;