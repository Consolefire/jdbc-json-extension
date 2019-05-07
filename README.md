# Jdbc JSON Extension 
`jdbc-json-extension` is to fetch data from RDBMS in JSON format. The data is structured the in the same relation structure of the Tables relationships in the database. The fetch is pre-planned using a `FetchPlan` configuration. The execution is multithreaded tree pattern where it starts from the `root` table with supplied filter parameters. The subsequent nodes (tables) are fetched as the `One-to-*` relationship defined from the root tables.

## Modules

	- Common `jdbc-json-extension-common`:
	- Core `jdbc-json-extension-core`:
