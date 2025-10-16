using System.Collections;
using UnityEngine;

public class EnemyAI : MonoBehaviour
{
    [Header("AI Settings")]
    [SerializeField] private float roamChangeInterval = 3f;
    [SerializeField] private float roamDistance = 5f;
    [SerializeField] private float attackRange = 5f;
    [SerializeField] private float attackCooldown = 2f;
    [SerializeField] private bool stopMovingWhileAttacking = false;
    [SerializeField] private MonoBehaviour enemyType; // Must implement IEnemy

    private EnemyPathfinding enemyPathfinding;
    private IEnemy shooter;
    private Vector2 roamTarget;
    private float roamTimer = 0f;
    private bool canAttack = true;

    private enum State { Roaming, Attacking }
    private State currentState = State.Roaming;

    private void Awake()
    {
        enemyPathfinding = GetComponent<EnemyPathfinding>();
        shooter = enemyType as IEnemy;

        if (shooter == null)
        {
            Debug.LogError("enemyType must implement IEnemy interface on: " + gameObject.name);
        }
    }

    private void Start()
    {
        roamTarget = GetNewRoamPosition();
    }

    private void Update()
    {
        if (PlayerController.Instance == null) return;

        float distanceToPlayer = Vector2.Distance(transform.position, PlayerController.Instance.transform.position);

        // Change state based on distance
        if (distanceToPlayer <= attackRange)
        {
            currentState = State.Attacking;
        }
        else
        {
            currentState = State.Roaming;
        }

        switch (currentState)
        {
            case State.Roaming:
                Roam();
                break;
            case State.Attacking:
                Attack();
                break;
        }
    }

    private void Roam()
    {
        roamTimer += Time.deltaTime;

        enemyPathfinding.MoveTo(roamTarget);

        if (roamTimer >= roamChangeInterval || Vector2.Distance(transform.position, roamTarget) < 0.5f)
        {
            roamTarget = GetNewRoamPosition();
            roamTimer = 0f;
        }
    }

    private void Attack()
    {
        Vector2 playerPos = PlayerController.Instance.transform.position;

        if (stopMovingWhileAttacking)
        {
            enemyPathfinding.StopMoving();
        }
        else
        {
            enemyPathfinding.MoveTo(playerPos);
        }

        if (canAttack)
        {
            canAttack = false;
            shooter.Attack();
            StartCoroutine(ResetAttackCooldown());
        }
    }

    private IEnumerator ResetAttackCooldown()
    {
        yield return new WaitForSeconds(attackCooldown);
        canAttack = true;
    }

    private Vector2 GetNewRoamPosition()
    {
        Vector2 randomDir = new Vector2(Random.Range(-1f, 1f), Random.Range(-1f, 1f)).normalized;
        return (Vector2)transform.position + randomDir * roamDistance;
    }

    private void OnDrawGizmosSelected()
    {
        Gizmos.color = Color.yellow;
        Gizmos.DrawWireSphere(roamTarget, 0.3f);
    }
}
